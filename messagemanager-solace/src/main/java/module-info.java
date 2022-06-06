import nl.queuemanager.solace.SolaceModule;

module messagemanager.solace {
    exports nl.queuemanager.solace;

    provides com.google.inject.Module with SolaceModule;

    requires messagemanager.core;
    requires messagemanager.jmsmessages;
    requires messagemanager.plugin.api;
    requires messagemanager.ui;
    requires sol.jms;

    requires jakarta.jms.api;
    requires com.google.common;
    requires com.google.guice;
    requires static lombok;

    requires java.desktop;
    requires java.management;
    requires javax.inject;
    requires java.logging;
}