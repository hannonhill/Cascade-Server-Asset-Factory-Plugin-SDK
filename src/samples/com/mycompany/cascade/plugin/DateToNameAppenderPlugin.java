/*
 * Created on Aug 20, 2010 by mike
 * 
 * Copyright(c) 2000-2011 Hannon Hill Corporation.  All rights reserved.
 */
package com.mycompany.cascade.plugin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.cms.assetfactory.BaseAssetFactoryPlugin;
import com.cms.assetfactory.PluginException;
import com.hannonhill.cascade.api.asset.admin.AssetFactory;
import com.hannonhill.cascade.api.asset.home.FolderContainedAsset;
import com.hannonhill.cascade.api.asset.home.Page;

/**
 * Appends today's date to the asset's name so that it looks like "<asset-name> yyyy-MM-dd".
 * 
 * @author  Mike Strauch
 * @since   6.8
 */
public class DateToNameAppenderPlugin extends BaseAssetFactoryPlugin
{

    /* (non-Javadoc)
     * @see com.cms.assetfactory.AssetFactoryPlugin#getName()
     */
    public String getName()
    {
        return "plugin.date.to.name.appender.name";
    }

    /* (non-Javadoc)
     * @see com.cms.assetfactory.AssetFactoryPlugin#getDescription()
     */
    public String getDescription()
    {
        return "plugin.date.to.name.appender.description";
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
        Map<String, String> pluginParamDescriptions = new HashMap<String, String>();
        return pluginParamDescriptions;
    }

    /* (non-Javadoc)
     * @see com.cms.assetfactory.BaseAssetFactoryPlugin#doPluginActionPre(com.hannonhill.cascade.api.asset.admin.AssetFactory, com.hannonhill.cascade.api.asset.home.FolderContainedAsset)
     */
    public void doPluginActionPre(AssetFactory factory, FolderContainedAsset asset) throws PluginException
    {
        // nothing to do here
    }

    /* (non-Javadoc)
     * @see com.cms.assetfactory.BaseAssetFactoryPlugin#doPluginActionPost(com.hannonhill.cascade.api.asset.admin.AssetFactory, com.hannonhill.cascade.api.asset.home.FolderContainedAsset)
     */
    public void doPluginActionPost(AssetFactory factory, FolderContainedAsset asset) throws PluginException
    {
        if (asset instanceof Page == false)
            return;

        Date today = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String assetName = asset.getName();
        assetName += " " + format.format(today);

        asset.setName(assetName);
    }
}
