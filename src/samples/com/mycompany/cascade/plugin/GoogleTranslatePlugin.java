/*
 * Created on Aug 20, 2010 by mike
 * 
 * Copyright(c) 2000-2011 Hannon Hill Corporation.  All rights reserved.
 */
package com.mycompany.cascade.plugin;

import java.util.HashMap;
import java.util.Map;

import com.cms.assetfactory.BaseAssetFactoryPlugin;
import com.cms.assetfactory.PluginException;
import com.google.api.translate.Language;
import com.google.api.translate.Translate;
import com.hannonhill.cascade.api.asset.admin.AssetFactory;
import com.hannonhill.cascade.api.asset.common.Metadata;
import com.hannonhill.cascade.api.asset.home.FolderContainedAsset;
import com.hannonhill.cascade.api.asset.home.MetadataAwareAsset;
import com.hannonhill.cascade.api.asset.home.Page;

/**
 * Uses a Google Translate library to translate the contents of a MetadataAwareAsset into a particular language.  
 * This plugin requires internet access.
 * 
 * @author  Mike Strauch
 * @since   6.8
 */
public class GoogleTranslatePlugin extends BaseAssetFactoryPlugin
{

    /* (non-Javadoc)
     * @see com.cms.assetfactory.AssetFactoryPlugin#getName()
     */
    public String getName()
    {
        return "plugin.google.translate.name";
    }

    /* (non-Javadoc)
     * @see com.cms.assetfactory.AssetFactoryPlugin#getDescription()
     */
    public String getDescription()
    {
        return "plugin.google.translate.description";
    }

    /* (non-Javadoc)
     * @see com.cms.assetfactory.AssetFactoryPlugin#getAvailableParameterNames()
     */
    public String[] getAvailableParameterNames()
    {
        return new String[]
        {
            "plugin.google.translate.param.language.name"
        };
    }

    /* (non-Javadoc)
     * @see com.cms.assetfactory.AssetFactoryPlugin#getAvailableParameterDescriptions()
     */
    public Map<String, String> getAvailableParameterDescriptions()
    {
        Map<String, String> paramDescs = new HashMap<String, String>();
        paramDescs.put("plugin.google.translate.param.language.name", "plugin.google.translate.param.language.description");
        return paramDescs;
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
        if (asset instanceof MetadataAwareAsset == false)
            return;

        MetadataAwareAsset mAwareAsset = (MetadataAwareAsset) asset;

        try
        {
            // Set the HTTP referrer to your website address.
            Translate.setHttpReferrer("hannonhill.com");
            String language = getParameter("plugin.google.translate.param.language.name");

            Metadata metadata = mAwareAsset.getMetadata();
            System.out.println("Got asset metadata");

            Language from = Language.ENGLISH;
            Language to = Language.fromString(language);

            System.out.println("languages are: to=" + to + " and from=" + from);

            if (metadata.getDisplayName() != null && !metadata.getDisplayName().equals(""))
                metadata.setDisplayName(Translate.execute(metadata.getDisplayName(), from, to));

            if (metadata.getTitle() != null && !metadata.getTitle().equals(""))
                metadata.setTitle(Translate.execute(metadata.getTitle(), from, to));

            if (metadata.getSummary() != null && !metadata.getSummary().equals(""))
                metadata.setSummary(Translate.execute(metadata.getSummary(), from, to));

            if (mAwareAsset instanceof Page)
            {
                Page page = (Page) mAwareAsset;
                page.setXHTML(Translate.execute(page.getXHTML(), from, to));
            }
        }
        catch (Exception e)
        {
            throw new PluginException(e);
        }
    }
}
