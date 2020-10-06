# MeLa (Mermaid Language)


- [1. Introduction](#introduction-pane)
- [2. Installation](#installation-pane)
- [3. Repository](#repository-pane)
- [4. MeLa manual and tutorial](#manual-pane)


## <a name="introduction-pane"></a> 1. Introduction

This repository has been created as an additional material for the article "MeLa: a programming language for a new multidisciplinary oceanographic float" publisher in the Sensors [https://www.mdpi.com/journal/sensors](https://www.mdpi.com/journal/sensors) journal in 2020. 

MeLa is a Domain Specific Language designed to write applications for the Mermaid instrument, a profiler float used to monitor marine environment. The instrument drifts with the oceanic current at depths up to 5000 meters and ascents to the surface at regular time intervals to transmit scientific data. The instrument intents to be a pluridisciplinary platform that can be programmed with different applications written by scientists of different domains such as meteorology, biology or geosciences. MeLa has been created to allow non-specialists of embedded systems to write their own applications for the instrument.

The language offers the following advantages:

* It hides embedded software details to developers.
* It takes account of the instrument limitations, such as energy or processing resources.
*  It gives the guarantee that the embedded software deployed on the instrument is safe and reliable.
*  It enables the programming of several applications on the same instrument.

The installation procedure to be able to use the language is in [chapter 2](#installation-pane). Information about folders of the repository is in [chapter 3](#repository-pane). A brief description of the MeLa syntax and semantic is in [chapter 4](#-pane). A list of functions implemented in the language is in [chapter 5](#repository-pane). Finally, [chapter 6](#repository-pane) is a tutorial to learn to use MeLa with three little applications.



## <a name="installation-pane"></a> 2. Installation

The project has been created in Java with the IntelliJ IDEA CE. Download and install the Community edition [(https://www.jetbrains.com/idea/download](https://www.jetbrains.com/idea/download)) and import the project by opening the [pom.xml](pom.xml) file.

To compile the project, click on the Maven icon that can be found on the bottom border or right border of the IDE. Double click on `compomaid > Lifecycle > compile` to start the compilation process. The message `BUILD SUCCESS` should be printed once the compilation has terminated.

To run the project, go on the project panel on the left side and open the folders `src > main > java > fr.uca.i3s.sparks.compomaid`, right-click on the `Compomaid` file and click on `Run 'Compomaid.main()'` (after it is possible to click on the "play" button on the top right of the editor). The project will read the applications in the `MeLaApps` folder, show information about resource usage of the instrument (processor, energy, satellite transmission) and generate code to simulate the application on a computer.This version of MeLa does not generate the for the instrument and the library necessary to compile the simulation code neither.



## <a name="repository-pane"></a> 3. Repository

The repository contains three directories.

* The `MeLaApps` directory contains the applications written in MeLa, with the `.mela` extension.
* The `generated` directory contains the code which can be compiled to execute the applications on a computer or install them on a Mermaid float.
* The `src` directory contains the Antlr [(https://www.antlr.org/)](https://www.antlr.org/) code that parses MeLa applications into the Java models and the tools for static analysis, application composition and code generation.



## <a name="manual-pane"></a> 4. MeLa manual and tutorial

A manual and tutorial is given in the `MeLaManual&Tuto.pdf` file.
