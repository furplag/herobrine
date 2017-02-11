# HerobrineAI
[![Build Status](http://jenkins.logicshard.com/buildStatus/icon?job=HerobrineAI)](http://jenkins.logicshard.com/job/HerobrineAI/)

=========

HerobrineAI is server-side bukkit plugin for Minecraft that creates Herobrine with custom AI and skin.  
Plugin has many features and it is based on the legend of Herobrine.  

Contributing 
---------
This is an old project and there are some parts of code that are poorly written, feel free to pull any refactored code.

Building
---------
**Maven with jdk7+ is required**

**1. Clone project**  
```
git clone git://github.com/jakub1221/HerobrineAI.git 
```

**2. Install maven with jdk**  
[Tutorial here](http://maven.apache.org/install.html)

**3. Install Craftbukkit**  
Plugin uses NMS code that is located in craftbukkit.  
In order to get craftbukkit installed to local maven repository you need to run [BuildTools](https://www.spigotmc.org/wiki/buildtools/).

**4. Build plugin**  
```
mvn clean install
```
Plugin will be located in target directory after building is done.

Downloads
---------
You can download latest build from jenkins site [here](http://jenkins.logicshard.com/job/HerobrineAI/).
