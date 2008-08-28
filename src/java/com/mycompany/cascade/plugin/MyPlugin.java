/*
 * Created Oct 13, 2006 by Zach Bailey
 */
package com.mycompany.cascade.plugin;

import java.util.HashMap;
import java.util.Map;

import com.cms.assetfactory.BaseAssetFactoryPlugin;
import com.cms.assetfactory.PluginException;
import com.hannonhill.cascade.api.asset.admin.AssetFactory;
import com.hannonhill.cascade.api.asset.home.FolderContainedAsset;

/**
 * This is a simple asset factory plugin.
 */
public final class MyPlugin extends BaseAssetFactoryPlugin
{
    /** The resource bundle key for the name of the plugin */
    private static final String NAME_KEY = "plugin.name";
    /** The resource bundle key for the description of the plugin */
    private static final String DESC_KEY = "plugin.description";
    /** The resource bundle key for the name of a parameter */
    private static final String SIMPLE_PARAM_NAME_KEY = "parameter.simple.name";
    /** The resource bundle key for the description of a parameter */
    private static final String SIMPLE_PARAM_DESC_KEY = "parameter.simple.description";

    /* (non-Javadoc)
     * @see com.cms.assetfactory.BaseAssetFactoryPlugin#doPluginActionPost(com.hannonhill.cascade.api.asset.admin.AssetFactory, com.hannonhill.cascade.api.asset.home.FolderContainedAsset)
     */
    @Override
    public void doPluginActionPost(AssetFactory factory, FolderContainedAsset asset) throws PluginException
    {
        //code in this method will be executed after the users submits the creation.
        //This could be used for data validation or post-population/property transfer.
    }

    /* (non-Javadoc)
     * @see com.cms.assetfactory.BaseAssetFactoryPlugin#doPluginActionPre(com.hannonhill.cascade.api.asset.admin.AssetFactory, com.hannonhill.cascade.api.asset.home.FolderContainedAsset)
     */
    @Override
    public void doPluginActionPre(AssetFactory factory, FolderContainedAsset asset) throws PluginException
    {
        //code in this method will be executed before the user is presented with the
        //initial edit screen. This could be used for pre-population, etc.
    }

    /* (non-Javadoc)
     * @see com.cms.assetfactory.AssetFactoryPlugin#getAvailableParameterDescriptions()
     */
    public Map<String, String> getAvailableParameterDescriptions()
    {
        //build a map where the keys are the names of the parameters
        //and the values are the descriptions of the parameters
        Map<String, String> paramDescriptionMap = new HashMap<String, String>();
        paramDescriptionMap.put(SIMPLE_PARAM_NAME_KEY, SIMPLE_PARAM_DESC_KEY);
        return paramDescriptionMap;
    }

    /* (non-Javadoc)
     * @see com.cms.assetfactory.AssetFactoryPlugin#getAvailableParameterNames()
     */
    public String[] getAvailableParameterNames()
    {
        //return a string array with all the name keys of
        //the parameters for the plugin
        return new String[] { SIMPLE_PARAM_NAME_KEY };
    }

    /* (non-Javadoc)
     * @see com.cms.assetfactory.AssetFactoryPlugin#getDescription()
     */
    public String getDescription()
    {
        //return the resource bundle key of this plugin's
        //description
        return DESC_KEY;
    }

    /* (non-Javadoc)
     * @see com.cms.assetfactory.AssetFactoryPlugin#getName()
     */
    public String getName()
    {
        //return the resource bundle key of this plugin's
        //name
        return NAME_KEY;
    }
}