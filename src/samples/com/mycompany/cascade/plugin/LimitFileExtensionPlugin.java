/*
 * Created on Aug 20, 2010 by mike
 * 
 * Copyright(c) 2000-2011 Hannon Hill Corporation.  All rights reserved.
 */
package com.mycompany.cascade.plugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cms.assetfactory.BaseAssetFactoryPlugin;
import com.cms.assetfactory.FatalPluginException;
import com.cms.assetfactory.PluginException;
import com.hannonhill.cascade.api.asset.admin.AssetFactory;
import com.hannonhill.cascade.api.asset.home.File;
import com.hannonhill.cascade.api.asset.home.FolderContainedAsset;

/**
 * Limits file uploads to configured extensions.
 * 
 * @author  Mike Strauch
 * @since   6.8
 */
public class LimitFileExtensionPlugin extends BaseAssetFactoryPlugin
{

    private static final String PLUGIN_PARAM_EXTENSIONS_NAME = "plugin.file.extension.restricter.param.extensions.name";

    /* (non-Javadoc)
     * @see com.cms.assetfactory.AssetFactoryPlugin#getName()
     */
    public String getName()
    {
        return "plugin.file.extension.restricter.name";
    }

    /* (non-Javadoc)
     * @see com.cms.assetfactory.AssetFactoryPlugin#getDescription()
     */
    public String getDescription()
    {
        return "plugin.file.extension.restricter.description";
    }

    /* (non-Javadoc)
     * @see com.cms.assetfactory.AssetFactoryPlugin#getAvailableParameterNames()
     */
    public String[] getAvailableParameterNames()
    {
        return new String[]
        {
            PLUGIN_PARAM_EXTENSIONS_NAME
        };
    }

    /* (non-Javadoc)
     * @see com.cms.assetfactory.AssetFactoryPlugin#getAvailableParameterDescriptions()
     */
    public Map<String, String> getAvailableParameterDescriptions()
    {
        Map<String, String> paramDescriptions = new HashMap<String, String>();
        paramDescriptions.put(PLUGIN_PARAM_EXTENSIONS_NAME, "plugin.file.extension.restricter.param.extensions.description");
        return paramDescriptions;
    }

    /* (non-Javadoc)
     * @see com.cms.assetfactory.BaseAssetFactoryPlugin#doPluginActionPre(com.hannonhill.cascade.api.asset.admin.AssetFactory, com.hannonhill.cascade.api.asset.home.FolderContainedAsset)
     */
    @Override
    public void doPluginActionPre(AssetFactory factory, FolderContainedAsset asset) throws PluginException
    {
        // prevent the system name from being changed
        asset.setHideSystemName(true);
    }

    /* (non-Javadoc)
     * @see com.cms.assetfactory.BaseAssetFactoryPlugin#doPluginActionPost(com.hannonhill.cascade.api.asset.admin.AssetFactory, com.hannonhill.cascade.api.asset.home.FolderContainedAsset)
     */
    @Override
    public void doPluginActionPost(AssetFactory factory, FolderContainedAsset asset) throws PluginException
    {
        System.out.println("LimitFileExtensionPlugin starting up!");
        if (asset instanceof File == false)
            return;

        String fileName = asset.getName();

        if (!fileName.contains("."))
            throw new FatalPluginException("File does not have an extension");

        String validExtensions = getParameter(PLUGIN_PARAM_EXTENSIONS_NAME);
        validExtensions = validExtensions.replaceAll(" ", "");
        List<String> extensionsList = Arrays.asList(validExtensions.split(","));

        int lastIndexOfPeriod = fileName.lastIndexOf('.');
        String extension = fileName.substring(lastIndexOfPeriod, fileName.length());
        System.out.println("extensions of file is :" + extension);

        if (!extensionsList.contains(extension))
            throw new FatalPluginException("File does not have a valid extension, must have one of the following: " + validExtensions);

        System.out.println("LimeFileExtensionPlugin finished!");
    }
}
