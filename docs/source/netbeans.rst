===================================
Importing the NMF into Netbeans IDE
===================================

.. contents:: Table of contents

Getting started
---------------
Netbeans is the recommended IDE to use with the NMF as it definitely works out of the box.
To import your NMF distribution into the IDE, select `File -> Open Project` and select the NMF
root directory that you cloned from GitHub. Netbeans will now import all Maven subprojects into the Netbeans
workspace.

Setting up the supervisor with simulator
----------------------------------------
Right click the project **ESA NMF Core Composite - NanoSat MO Supervisor** and select the option **Properties**.
In the options **Run** enter the path to the supervisor simulator environment (by default at **sdk/sdk-package/target/nmf-sdk-2.0.0-SNAPSHOT/home/nmf/nanosat-mo-supervisor-sim**). Then add the following line under VM Options and save the configuration.

```
-Dnmf.platform.impl=esa.mo.platform.impl.util.PlatformServicesProviderSoftSim
```


Setting up the CTT
------------------
Right click the project **ESA NMF SDK Tool - Consumer Test Tool (CTT)** and select the option **Properties**.
In the options **Run** enter the path to the CTT execution environment (by default at **sdk/sdk-package/target/nmf-sdk-2.0.0-SNAPSHOT/home/nmf/consumer-test-tool**) and save the configuration.

You are now able to start the supervisor with simulator and the CTT from NetBeans by right-clicking the respective project and selecting **Run**.
You can now look at :doc:`apps/apps`. 
