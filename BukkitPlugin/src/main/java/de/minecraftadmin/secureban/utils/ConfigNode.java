package de.minecraftadmin.secureban.utils;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 24.12.12
 * Time: 14:36
 * To change this template use File | Settings | File Templates.
 */
public enum ConfigNode {
    ActivateGlobalBanCommand("command.global.active"),
    OverrideGlobalBanCommand("command.global.override"),
    ActivateLocalBanCommand("command.local.active"),
    OverrideLocalBanCommand("command.local.override"),
    ActivateTempBanCommand("command.temp.active"),
    OverrideTempBanCommand("command.temp.override"),
    MultiServer("multiserver"),
    SAVEKICKTODB("saveKick");
    private final String configNode;

    ConfigNode(String node) {
        this.configNode = node;
    }

    public String getNode() {
        return configNode;
    }
}
