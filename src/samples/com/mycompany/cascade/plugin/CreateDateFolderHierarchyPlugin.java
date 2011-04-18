/*
 * Created on Aug 19, 2010 by mike
 * 
 * Copyright(c) 2000-2011 Hannon Hill Corporation.  All rights reserved.
 */
package com.mycompany.cascade.plugin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cms.assetfactory.BaseAssetFactoryPlugin;
import com.cms.assetfactory.FatalPluginException;
import com.cms.assetfactory.PluginException;
import com.hannonhill.cascade.api.asset.admin.AssetFactory;
import com.hannonhill.cascade.api.asset.common.Identifier;
import com.hannonhill.cascade.api.asset.home.Folder;
import com.hannonhill.cascade.api.asset.home.FolderContainedAsset;
import com.hannonhill.cascade.api.operation.Create;
import com.hannonhill.cascade.api.operation.Read;
import com.hannonhill.cascade.api.operation.result.CreateOperationResult;
import com.hannonhill.cascade.api.operation.result.ReadOperationResult;
import com.hannonhill.cascade.model.dom.identifier.EntityType;
import com.hannonhill.cascade.model.dom.identifier.EntityTypes;

/**
 * A plugin that creates a folder hierarchy based on today's date to put the new asset in.
 * 
 * @author  Mike Strauch
 * @since   6.8
 */
public class CreateDateFolderHierarchyPlugin extends BaseAssetFactoryPlugin
{

    /* (non-Javadoc)
     * @see com.cms.assetfactory.AssetFactoryPlugin#getName()
     */
    public String getName()
    {
        return "plugin.folder.hierarchy.generator.name";
    }

    /* (non-Javadoc)
     * @see com.cms.assetfactory.AssetFactoryPlugin#getDescription()
     */
    public String getDescription()
    {
        return "plugin.folder.hierarchy.generateor.description";
    }

    /* (non-Javadoc)
     * @see com.cms.assetfactory.AssetFactoryPlugin#getAvailableParameterNames()
     */
    public String[] getAvailableParameterNames()
    {
        return new String[0];
    }

    /* (non-Javadoc)
     * @see com.cms.assetfactory.AssetFactoryPlugin#getAvailableParameterDescriptions()
     */
    public Map<String, String> getAvailableParameterDescriptions()
    {
        return new HashMap<String, String>();
    }

    /* (non-Javadoc)
     * @see com.cms.assetfactory.BaseAssetFactoryPlugin#doPluginActionPre(com.hannonhill.cascade.api.asset.admin.AssetFactory, com.hannonhill.cascade.api.asset.home.FolderContainedAsset)
     */
    @Override
    public void doPluginActionPre(AssetFactory factory, FolderContainedAsset asset) throws PluginException
    {
        // nothing to do here
    }

    /* (non-Javadoc)
     * @see com.cms.assetfactory.BaseAssetFactoryPlugin#doPluginActionPost(com.hannonhill.cascade.api.asset.admin.AssetFactory, com.hannonhill.cascade.api.asset.home.FolderContainedAsset)
     */
    @Override
    public void doPluginActionPost(AssetFactory factory, FolderContainedAsset asset) throws PluginException
    {
        log("Starting NameByDatePlugin!");
        Date today = new Date();
        // will convert any date into a particular format
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = dateFormat.format(today);

        Folder parentFolder = asset.getParentFolder();
        log("Attempting to build folder hierarchy with path '" + formattedDate + "' inside parent folder with path '" + parentFolder.getPath() + "'");
        Folder datedParentFolder = buildSubFolders(formattedDate, parentFolder);

        asset.setParentFolder(datedParentFolder);
        log("Finished NameByDatePlugin!");
    }

    /**
     * Builds a folder hierarchy based on the subPath supplied.  SubPath must be of the form "folder1/folder2/folder3".
     *
     * @param subPath
     * @param startingFolder
     * @return
     */
    private Folder buildSubFolders(String subPath, Folder startingFolder) throws PluginException
    {
        List<String> pathSegments = new ArrayList<String>();
        pathSegments.addAll(Arrays.asList(subPath.split("/")));

        Folder currentParent = startingFolder;
        // keep digging down into the folder hierarchy until there is a folder missing
        // then create the remaining missing folders and return the folder that is 
        // the deepest in the hierarchy
        while (pathSegments.size() > 0)
        {
            String currentSegment = pathSegments.get(0);
            Folder subFolder = findFolderWithName(currentSegment, currentParent);
            if (subFolder == null)
            {
                log("Folder hierarchy is missing a folder, attempting to create remainging folders.  Folder missing was '" + currentSegment + "'");
                return createFolderHierarchy(pathSegments, currentParent);
            }

            currentParent = subFolder;
            pathSegments.remove(0);
        }

        log("All segments already exist, no folders needed to be created");
        return currentParent;

    }

    /**
     * Attempts to find a Folder with a particular name inside a parent Folder.
     * 
     * @param name
     * @param parentFolder
     * @return
     */
    private Folder findFolderWithName(String name, Folder parentFolder)
    {
        log("Attempting to find folder with name '" + name + "' in parent folder '" + parentFolder.getPath() + "'");
        for (FolderContainedAsset asset : parentFolder.getChildren())
        {
            if (asset instanceof Folder && asset.getName().equals(name))
                return (Folder) asset;
        }

        return null;
    }

    /**
     * Takes a list of path segments and a starting folder and generates a folder hierarchy
     * by iterating over the path segments and generating new folders for each. 
     * 
     * @param pathSegments
     * @param startingFolder
     * @return
     */
    private Folder createFolderHierarchy(List<String> pathSegments, Folder startingFolder) throws PluginException
    {
        Folder currentParent = startingFolder;
        for (String segment : pathSegments)
            currentParent = createFolder(segment, currentParent);

        return currentParent;
    }

    /**
     * Creates a new Folder with the given name inside the given parent folder.
     * 
     * @param folderName
     * @param parentFolder
     * @return
     */
    private Folder createFolder(String folderName, Folder parentFolder) throws PluginException
    {
        Create folderCreateOperation = new Create();
        log("Creating folder with name: " + folderName + " inside folder with name: " + parentFolder.getName());

        try
        {
            Folder toCreate = (Folder) Create.newAPIAsset(parentFolder);
            toCreate.setParentFolder(parentFolder);
            toCreate.setName(folderName);

            folderCreateOperation.setCreateNewInstance(true);
            folderCreateOperation.setInstantiateWorkflow(false);
            folderCreateOperation.setUsername(getUsername());
            folderCreateOperation.setAsset(toCreate);

            CreateOperationResult result = (CreateOperationResult) folderCreateOperation.perform();

            Read readNewlyCreatedFolder = new Read();
            readNewlyCreatedFolder.setUsername(getUsername());
            readNewlyCreatedFolder.setToRead(new AssetIdentifier(result.getCreatedAssetId(), EntityTypes.TYPE_FOLDER));
            ReadOperationResult readResult = (ReadOperationResult) readNewlyCreatedFolder.perform();
            return (Folder) readResult.getAsset();

        }
        catch (Exception e)
        {
            throw new FatalPluginException("Unable to create Folder with name '" + folderName + "' inside folder '" + parentFolder.getPath() + "'");
        }

    }

    /**
     * Outputs a String to System.out
     * @param message
     */
    private void log(String message)
    {
        System.out.println(message);
    }

    class AssetIdentifier implements Identifier
    {
        private String id;
        private EntityType type;

        public AssetIdentifier(String id, EntityType type)
        {
            this.id = id;
            this.type = type;
        }

        /* (non-Javadoc)
         * @see com.hannonhill.cascade.api.asset.common.Identifier#getId()
         */
        public String getId()
        {
            return id;
        }

        /* (non-Javadoc)
         * @see com.hannonhill.cascade.api.asset.common.Identifier#getType()
         */
        public EntityType getType()
        {
            return type;
        }
    }
}
