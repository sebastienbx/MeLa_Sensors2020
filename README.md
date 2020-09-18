# MeLa (Mermaid Language)


- [1. Introduction](#introduction-pane)
- [2. Installation](#installation-pane)
- [3. Repository](#repository-pane)
- [4. Language description](#language-pane)
	- [4.1. Mission configuration](#mission-config-pane)
	- [4.2. Coordinator](#coordinator-pane)
	- [4.3. Acquisition modes](#acq-mode-pane)
	- [4.4. instructions](#instructions-pane)
- [5. Data types](#data-types-pane)
- [6. Library](#library-pane)
	- [6.1 Array functions](#array-functions-pane)
	- [6.2 Buffer functions](#buffer-functions-pane)
	- [6.3 Processing functions](#processing-functions-pane)
	- [6.4 Utility functions](#utility-functions-pane)
- [7. Tutorial](#tutorial-pane)


<!---
	- [6.1. app1: continuous record of temperature](#app1-pane)
	- [6.2. app2: detection of low range](#app2-pane)
	- [6.3. app1 and app2 composition](#app1Uapp2-pane)
	- [6.4. app3: continuous record of range sensor](#app3-pane)
	- [6.5. app1Uapp2 and app3 composition](#app1Uapp2Uapp3-pane)
-->



## <a name="introduction-pane"></a> 1. Introduction

This repository has been created as an additional material for the article "MeLa: a programming language for a new multidisciplinary oceanographic float" publisher in the Sensors [https://www.mdpi.com/journal/sensors](https://www.mdpi.com/journal/sensors) journal in 2020. 

MeLa is a Domain Specific Language designed to write applications for the Mermaid instrument, a profiler float used to monitor marine environment. The instrument drifts with the oceanic current at depths up to 5000 meters and ascents to the surface at regular time intervals to transmit scientific data. The instrument intents to be a pluridisciplinary platform that can be programmed with different applications written by scientists of different domains such as meteorology, biology or geosciences. MeLa has been created to allow non specialists of embedded systems to write their own applications for the instrument.

The language offers the following advantages:

* It hides embedded software details to the developpers.
* It takes account of the instrument limitations, sucha as energy or processing ressources.
*  It gives the guarantee that the embedded software deployed on the instrument is safe and reliable.
*  It enable the programming of several applications on the same instrument.

The installation procedure to be able to use the language is in [chapter 2](#installation-pane). Information about folders of the repository is in [chapter 3](#repository-pane). A brief description of the MeLa syntax and semantic is in [chapter 4](#-pane). A list of functions implemented in the language is in [chapter 5](#repository-pane). Finally, [chapter 6](#repository-pane) is a tutorial to learn to use MeLa with three little applications.



<!-- This software is a prototype of a development environment dedicated to an oceanographic instrument. The instrument is a Lagrangian float that drifts with the oceanic current at depths up to 2000m and ascents to the surface at regular time intervals to transmit scientific data. The instrument intents to be a pluridisciplinary platform that can be programmed with different applications written by scientists who are specialists of their domains like meteorology, biology or geosciences. Several applications written independently by different specialists can be put together in the same instrument. -->

<!-- To develop an application, oceanologists have access to a domain specific language and a library of function to read sensors, process and record data or execute specific actions like asking for the float to ascend. The domain specific language has a limited scope intended to ease the development by reducing the programming possibilities. The library contains some specific information about resource consumption of the platform like processing time or energy, in this document we use the word *annotation* to designate this information. The annotations allow for the verification of properties specific to the embedded software domain and give feedback to the users. For example, the scheduling analysis gives feedback about the execution time of the application and the energy analysis returns an estimation of the instrument autonomy. -->

<!-- In order to install applications written independently in the same instrument, we provide a composition algorithm that can group two applications into a single one that can be uploaded into the instrument. The composition algorithm is actually very simple but our future work will focus on how to detect and resolve conflict between the applications and how to share application functionalities in order to optimize resources consumption of the applications. The composition is done before the installation on the instrument and not during the execution like with a regular OS like Android because the instrument has too limited resources (processor, memory, energy, communication) for this kind of system. -->

<!-- The tutorial in [chapter 6](#tutorial-pane) is a prototype demonstrator made with a [Grove Starter Kit for Arduino](https://www.seeedstudio.com/Grove-Starter-Kit-for-Arduino-p-1855.html) from SeeedStudio. In this example we use the temperature sensor of the kit and the [Grove Ultrasonic Ranger](https://www.seeedstudio.com/Grove-Starter-Kit-for-Arduino-p-1855.html) (apart of the kit). These hardware parts are useful to test the applications for real but there are not mandatory for the most important part of the tutorial that concerns the development and composition of applications. -->



## <a name="installation-pane"></a> 2. Installation

The project has been created in Java with the IntelliJ IDEA CE. Download and install the Community edition [(https://www.jetbrains.com/idea/download](https://www.jetbrains.com/idea/download)) and import the project by opening the [pom.xml](pom.xml) file.

To compile the project, click on the Maven icon that can be found on the bottom border or right border of the IDE. Double click on `compomaid > Lifecycle > compile` to start the compilation process. The message `BUILD SUCCESS` should be printed once the compilation has terminated.

To run the project, go on the project pannel on the left side and open the folders `src > main > java > fr.uca.i3s.sparks.compomaid`, rigth click on the `Compomaid` file and click on `Run 'Compomaid.main()'` (after it is possible to click on the "play" button on the top right of the editor). The project will read the applications in the `MeLaApps` folder, show information about ressources usage of the instrument (processor, energy, satelitte transmission) and generate code to simulate the application on a computer.This version of MeLa does not generate the for the instrument and the the library necessary to compile the simulation code neither.

<!-- The PlantUML plugin is used in the project and can be installed by going in "Preferences..." and then in the "Plugins" section, click on browse repositories and search for "PlantUML integration" and install the plug in. The recommended way to open the Gitub project is to double-click on the  file, IntelliJ will then automatically import the project.  -->

<!-- To test the developed applications on a real platform you need to compile the generated source code with the [Arduino IDE](https://www.arduino.cc/en/Main/software). Then copy the content of the "ArduinoLibrary" directory to your local "Arduino/libraries" directory created during the installation of the Arduino IDE. -->

<!-- In the final version of the real instrument, the development environment will not change but the compilation platform will be changed for one's adapted to the real instrument. -->



## <a name="repository-pane"></a> 3. Repository

The repository contains three directories.

* The `MeLaApps` directory contains the applications written in MeLa, with the `.mela` extension.
* The `generated` directory contains the code which can be compiled to execute the applications on a computer or install them on a Mermaid float.
* The `src` directory contains the Antlr [(https://www.antlr.org/)](https://www.antlr.org/) code that parses MeLa applications into the Java models and the tools for static analysis, application composition and code generation.



## <a name="language-pane"></a> 4. Language description


A MeLa application is composed of three main parts:

* The mission configuration to define basic parameters of the instrument.
* The coordinator to define when to execute acquisition mode.
* The acquisition mode(s) to acquire and process the sensors data. There are two types of acquisition modes, the `ContinuousAcqMode` that processes data without stoping, and the `ShortAcqMode` that processes only one packet of data. 

```
Mission:
   ...

Coordinator:
   ...

ContinuousAcqMode acq1:
	...

ShortAcqMode acq2:
	...	
```



### <a name="mission-config-pane"></a> 4.1. _Mission configuration_

The mission configuration allows to define the depth of the dive, and the maximum time that the instrument can pass at this depth.

```
Mission:
   ParkTime: 14400 minutes;
   ParkDepth: 1500 meters;
```

<!-- The descent and ascent time are calculated from the park depth specified by the user, and from the default descent and ascent speed of the float, which are respectively 3 cm/s and 8 cm/s. For a depth of 100m the descent time is 10000/3/60 = 55 minutes and the ascent time is 10000/8/60 = 20 minutes. In order to reduce the simulation time on the Arduino platform, minutes are transformed to seconds, one minute on the real instrument is equivalent one second on the simulator. -->



### <a name="coordinator-pane"></a> 4.2. _Coordinator_

The coordinator defines when to execute an acquisition mode during the steps of a dive (i.e., descent, park and ascent). For `ShortAcqMode`, a time interval for each execution must be defined. This is not needed for `ContinuousAcqMode` that never stop during the step in which it is executed.  

```
Coordinator:
   DescentAcqModes: acq1;
   ParkAcqModes: acq1, acq2 every 10 minutes;
   AscentAcqModes: acq2 every 10 minutes;
```



### <a name="acq-mode-pane"></a> 4.3. _Acquisition modes_

A `ContinuousAcqMode` is executed continuously, it processes data in a streamed way, without stopping. It is more adapted to monitor sporadic events (i.e., that appends from time to time), but it can uses a lot of processor time, especially if the sampling of the sensor is high.

A `ContinuousAcqMode` is divided in several parts:

* The `Input` part allows to define the sensor to use and its configuration, for example it can be the hydrophone with a sampling frequency of 200 Hz.
* The `Variables` part allows to define the variables used to process the data. A list of data types is given in [chapter 5](#data-types-pane)
* The `RealTimeSequence` part contains instructions to process the data in real time. It gives the guaranty that all the data will be processed, without missing a sample, such that the data cannot be truncated. Only one `RealTimeSequence` can be defined. 
* The `ProcessingSequence` part is optional but can be used for instructions with an execution time too long for the `RealTimeSequence`. When this sequence is called there is no guarantee that all the samples coming from the sensor can be processed. It is possible to define several `ProcessingSequence` that can be called from the `RealTimeSequence` or from an other `ProcessingSequence`.

```
ContinuousAcqMode acq1:

Input:
   HydrophoneBF(40);

Variables:
   Int i;
   ArrayFloat array(10);
	
RealTimeSequence seq1:
   ...
endseq;

ProcessingSequence seq2:
   ...
endseq;
```


A `ProcessingAcqMode` have a very similar structre, the only difference is that it does not contain a `RealTimeSequence` because only one paket of data is processed.



### <a name="instructions-pane"></a> 4.4. _Instructions_

Instructions are called inside the sequences of instructions (i.e., `RealTimeSequence` or `ProcessingSequence`). The instructions can be:

* An operation, such as `c = a + b`. The operations only accept two operand for the current version of MeLa.
* A function call, such as `mean(r, array);` which compute the mean values of the array and put the result in the `r` variable.  The list of currently available functions is given in [chapter 6](#library-pane).
* A `if`condition, such `if a > 10 && b > c:`. A condition must also contain a probability that is used by MeLa to compute the attery lifetime of the float and the amount of data transmitted each month.
* A `for` loop, such as `for i, v in array:`. Each loop iteration read an element of the array from the first to the last, the index of the current element is put in the `i` variable, and its value in the `v` variable.

Comments can be writen this way  `/* this is a comment */`


```
RealTimeSequence seq1:
   
   /* Add two variables */
   c = a + b;	
   
   /* If variables exceed a value, compute the mean of the array */
   if a > 10 && b > c:
      @probability = 1 per hour
      mean(r, array);
   endif;
   
   /* Add each element of the array to the a variable */
   for i, v in array:
      a = a + v;
   endfor;
   
endseq;
```


## <a name="data-types-pane"></a> 5. Data types

**Basic types**

Basic data types are boolean, integers, floating point, complex numbers either integer of floating point and strings. To declare a variable, write the data type followed by the name of the variable.

```
Bool b;
Int i;
Float f;
ComplexInt ci;
ComplexFloat cf;
String str;
```


**Arrays**

Arrays can be defined for integers, floating points and complex numbers. To declare an array variable, write the data type, the name and the size of the array in parenthesis. The array can also be initialized with values added between the parethesis next to the array size (array of complex numbers cannot be initialized currently).

Parameter:

1. Length of the array.
2. (optional) Initialization values. The number of values must be equal to the length of the array.

```
ArrayInt ai1(10);
ArrayInt ai2(10, 1, 2, 3, 4, 5, 6, 7, 8, 9);

ArrayFloat af1(10);
ArrayFloat af2(10, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);

ArrayComplexInt aci(10);

ArrayComplexFloat acf(10);
```


**Buffers**

Buffers are similar to arrays but it is also possible to append data at the end of the buffer, if older data exists they are overwritten. This feature is practical to bufferize long series of data in a `ContinuousAcqMode`. This is not possible to append data to an array, however, an array can be converted to a buffer with the `toBuffer` function, and a buffer to an array with the `toArray` function. As for an array, the sie of the buffer must be defined in parenthesis.

Parameter:

1. Length of th buffer.

```
BufferInt bi(10);
BufferFloat bf(10);
```


**FFT**

Fast Fourier Transform (FFT) require a specific variable to be defined for the computation. The s

Parameter:

1. Length of the FFT. The length is limited to the following values 32, 64, 128, 256, 512, 1024, 2048 and 4096.

```
FFTInt fi(128);
FFTFloat ff(1024);
```


**IIR**

Infinite Impulse Response filters (IIR) are defined with theire coeafficients. In parenthesis, the first number is the , the second number is the , the next numbers are the  followed by thoose of the.

Parameters:

1. Order of the numerator.
2. Order of the denominator.
3. Coefficients of the numerator, the number of coefficients must be equal to the numerator order.
4. Coefficients of the denominator, the number of coefficients must be equal to the denominator order.

```
IIRInt ii(2, 5,
          1, -2,
          1, -2, 3, -4, 5);
IIRFloat if(2, 5,
            0.1, -0.2,
            0.1, -0.2, 0.3, -0.4, 0.5);
```


**FIR**

Finite Impulse Response filters (FIR) are defined as IIR but do not have denominator.

Parameters:

1. Order of the filter.
2. Coefficients, the number of coefficients must be equal to the filter order.

```
FIRInt fi(2,
          1, -2);
FIRFloat ff(2,
            0.1, -0.2);
```


**CDF24**

The CFD24 is a specific implementation of a wavelet transform originaly used in the Mermaid algorithm<sup>1</sup>. 

Parameters:

1. Number of scales of the transform. 
2. (Maximum?) Length of the data to process.

```
CDF24Int cdi(6, 12000);
CDF24Float cdf(6, 12000);
```

<sub>(1) Sukhovich et al., (2011), "Automatic discrimination of underwater acoustic signalsgenerated by teleseismic P-waves: A probabilistic approach", Geophysical Research Letters</sub>


**STALTA**

Short Term Average over Long Term Average (STA/LTA) is commonly found for seismic processing. 

Parameters:

1. Length of the short term average.
2. Length of the long term average.
3. Delay for the LTA, in the example below the STA is the average of the samples between index 0 and 399, and the LTA is the avergae between the index 400 and 4399.
4. (only for integers) Scaling factor for the result. It allows to obtain a better precision when the ratio of the average is close to 1.

```
StaLtaInt sti(400, 4000, 400, 1000);
StaLtaFloat stf(400, 4000, 400);
```


**Trigger**

The trigger is very useful to compare a signal with a value. When it is used by calling the appropriate function (see the trigger function in [chapter 5]()), the trigger return a boolean value that is true false depending of the parameters defined here. 

Parameters:

1. Define the mode of activation of the trigger (i.e., when it returns `true`). There are four possible modes
	1. HIGH: the trigger is activated if the signal is above a threshold value.
	2. LOW: the trigger is activated if the signal is under a threshold value.
	3. RISING\_EDGE: the trigger is activated if the signal pass above a threshold value, only the instant when it pass from a lower to a higher level.
	4. FALLING\_EDGE: the trigger is activated if the signal is above a threshold value, only the instant when it pass from a higher to a lower level.
2. This is the threshold value which is compared with the signal.
3. This is a delay to activate the trigger, for example it can be used to wait 4000 samples after a signal pass over a threshold in order to have more data to process.
4. This is the minimum time between each trigger, it can be used to ignore successive triggers close to each others.

```
TriggerInt ti(RISING_EDGE, 2500, 4000, 10000);
TriggerFloat tf(HIGH, 2.5, 4000, 10000);
```


**Distribution**

This type of data is used by the distribution functions ([chapter 5]()).

Parameters:

1. Length of the distribution.
2. x-axis values, the number of values must be equal to the length of the distribution.
3. y-axis values, the number of values must be equal to the length of the distribution.

```
DistributionInt di (10,
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        1, 3, 4, 9, 12, 11, 7, 5, 2, 0);
DistributionFloat df (10,
        +1.502314400e-01, +1.676934000e-01, +1.851553500e-01, +2.026173100e-01,
        +2.200792700e-01, +2.375412200e-01, +2.550031800e-01, +2.724651300e-01,
        +9.884053400e-01, +1.005867300e+00,
        +1.290000000e+02, +2.170000000e+02, +3.260000000e+02, +4.460000000e+02,
        +4.100000000e+01, +3.700000000e+01, +3.200000000e+01, +2.900000000e+01,
        +2.600000000e+01, +2.300000000e+01);
```


**File**

Allows to declare a file.

No parameters.

```
File f;
```


## <a name="library-pane"></a> 6. Library

The library contain all the functions accessible to process the data. We describe here the purpose of eaach function and how to use it. In addition, the function are associated with information of execution time, but this is not reported in this document.


### <a name="array-functions-pane"></a> 6.1. _Array functions_

**put(array, index, value)**

Put a value in an array at a specified index.

Types accepted:

```
put(ArrayInt array, Int index, Int value);
put(ArrayFloat array, Int index, Float value);
put(ArrayComplexInt array, Int index, ComplexInt value);
put(ArrayComplexFloat array, Int index, ComplexFloat value);
```

Example:

```
ArrayFloat a(10);
Float v;

/* Put v value at index 5 */
put(a, 5, v);

/* Put 1.6 value at index 8 */
put(a, 8, 1.6);
```

**get(array, index, value)**

Get a value in an array at a specified index.

Types accepted:

```
get(ArrayInt array, Int index, Int value);
get(ArrayFloat array, Int index, Float value);
get(ArrayComplexInt array, Int index, ComplexInt value);
get(ArrayComplexFloat array, Int index, ComplexFloat value);
```

Example:

```
ArrayFloat a(10);
Float v;

/* Get the value at index 5 and put it in v */
get(a, 5, v);
```

**copy(array1, index1, array2, index2, length)**

Copy the content of array1 starting from index1 and for the specified length to the array2 starting from index2.

Types accepted:

```
copy(ArrayInt array1, Int index1, ArrayInt array2, Int index2, Int length);
copy(ArrayFloat array1, Int index1, ArrayInt array2, Int index2, Int length);
copy(ArrayComplexInt array1, Int index1, ArrayInt array2, Int index2, Int length);
copy(ArrayComplexFloat array1, Int index1, ArrayInt array2, Int index2, Int length);
```

Example:

```
ArrayInt a(10);
ArrayInt b(10);
ArrayInt c(40);

/* Copy the content of 'a' in the first half part of 'c' */
copy(a, 0, c, 0, 10);

/* Copy half of the content of 'b' in the second half part of 'c' */
copy(b, 5, c, 20, 5);
```

TODO: Use the select function when it is needed to choose a part of the array to copy.


**toComplex(array1, array2)**

Copy the values of array1 to the real part of the complex array2. The imaginary part of array2 are all set to 0.

TODO: put it with the copy functions and allow specify to copy both imaginary and real part.

TODO: Remove complex types? they are only used to compute the FFT.

<!--
```
toComplex(ArrayInt array, ArrayComplexInt complexArray);
toComplex(ArrayFloat array, ArrayComplexFloat complexArray);
```
-->

**toFloat(array1, array2)**

Convert an integer array into a floating point array.

TODO: put it with the copy functions.


**toInt(array1, array2)**

Convert a floating point array into an integer array.

TODO: put it with the copy functions.


**select(array, index1, index2)**

Allow to select a specific portion of an array to work on. Once a portion of the array is selected all the following operation are done on the selected portion of the array, until it is unselected using the `unselect` function.


Types accepted:

```
select(ArrayInt array, Int index1, Int index2);
select(ArrayFloat array, Int index1, Int index2);
select(ArrayComplexInt array, Int index1, Int index2);
select(ArrayComplexFloat array, Int index1, Int index2);
```

Example:

```
ArrayInt a(100);

/* Select a portion of the 'a' array between index 50 and 69 (included),
   the lenght of the selected portion is 20 */
select(a, 50, 69);
```


**unselect(array)**

Cancel the effect of the select command.

Types accepted:

```
unselect(ArrayInt array);
unselect(ArrayFloat array);
unselect(ArrayComplexInt array);
unselect(ArrayComplexFloat array);
```

Example:

```
ArrayInt a(10);

unselect(a);
```


**clear(array)**

Set all values of an array to 0.

Types accepted:

```
clear(ArrayInt array);
clear(ArrayFloat array);
clear(ArrayComplexInt array);
clear(ArrayComplexFloat array);
```

Example:

```
ArrayInt a(10);

clear(a);
```


### <a name="buffer-functions-pane"></a> 6.2. _Buffer functions_

**push(buffer, value)**

Add a value to the end of the buffer.

Types accepted:

```
push(BufferInt buffer, Int value);
push(BufferFloat buffer, Float value);
push(BufferInt buffer, ArrayInt value);
push(BufferFloat buffer, ArrayFloat value);
```

Example:

```
BufferInt b(10);
ArrayInt a(10);

/* Add values of the array to the end of the buffer */
push(b, a);
```

**toArray(buffer, array)**

Copy the content of a buffer to an array.

TODO: put in the copy function.



### <a name="processing-functions-pane"></a> 6.3. _Processing functions_

**fft(fftv, array)**

Compute a Fast Fourier Fourier transform. Parameters of the FFT must be defined in the variable 'fftv'. Input data are overwritten by the results of the transform.

Types accepted:

```
fft(FFTInt fftv, ArrayComplexInt array);
fft(FFTFloat fftv, ArrayComplexFloat array);
```

Example:

```
FFTInt fi(128);
ArrayComplexInt a(128);

fft(fi, a);
```

**cdf24(cdf24v, array)**

Compute a CDF24 wavelet transform. Parameters of the CDF24 must be defined in the variable 'cdf24v'. Input data are overwritten by the results of the transform.


Types accepted:

```
cdf24(CDF24Int cdf24v, ArrayInt array);
cdf24(CDF24Float cdf24v, ArrayFloat array);
```

Example:

```
CDF24Int cdi(6, 12000);
ArrayInt a(12000);

cdf24(cdi, a);
```


**cdf24ScalesPower(cdf24v, array1, index1, index2, array2)**

Compute the power of each scale of the CDF24 from array1.  The index1 and index2 values allow to select a subset of the CDF24. The results are put in array2 which must have a size at least equal to the number of computed scales. 

Types accepted:

```
cdf24ScalesPower(CDF24Int cdf24v, ArrayInt array1, Int index1, Int index2, ArrayInt array2);
cdf24ScalesPower(CDF24Float cdf24v, ArrayFloat array1, Int index1, Int index2, ArrayFloat array2);
```

Example:

```
CDF24Int cdi(6, 12000);
ArrayInt a(12000);
ArrayInt p(6);

/* Compute the power on the first half part of the signal */
cdf24ScalesPower(cdi, a, 0, 5999, 6);
```


**iir(iirv, array1, array2)**

Infinite impulse response filter. The coefficients of the filter must be set in the variable 'iirv'. The 'array1' must contain the input data, the 'array2' contains the filtered data.

Types accepted:

```
iir(IIRInt iirv, ArrayInt array1, ArrayInt array2);
iir(IIRFloat iirv, ArrayFloat array1, ArrayFloat array2);
```

Example:

```
IIRInt ii(2, 5,
          1, -2,
          1, -2, 3, -4, 5);
ArrayInt a(100);
ArrayInt b(100);

/* Filter the signal contained in 'a' */
iir(ii, a, b);
```


**fir(firv, array1, array2)**

Finite impulse response filter. The coefficients of the filter must be set in the variable 'firv'. The 'array1' must contain the input data, the 'array2' contains the filtered data.

Types accepted:

```
fir(FIRInt firv, ArrayInt array1, ArrayInt array2);
fir(FIRFloat firv, ArrayFloat array1, ArrayFloat array2);
```


**stalta(staltav, input, output)**

Short term over long term average. The coefficients of the STA/LTA must be set in the variable 'staltav'.

Types accepted:

```
stalta(StaLtaInt staltav, Int input, Int output);
stalta(StaLtaFloat staltav, Float input, Float output);
stalta(StaLtaInt staltav, ArrayInt input, ArrayInt output);
stalta(StaLtaFloat staltav, ArrayFloat input, ArrayFloat output);
```


**trigger(triggerv, input, output)**

Return true for a high or low value, or a rising or falling edge, compared to a threshold. The parameters of the trigger must be defined in the variable 'triggerv'. The input can be array, in this case the output is set to true if at least one value in the array has produce a trigger.

Types accepted:

```
trigger(Triggerint triggerv, Int input, Bool output);
trigger(TriggerFloat triggerv, Float input, Bool output);
trigger(TriggerInt triggerv, ArrayInt input, Bool output);
trigger(TriggerFloat triggerv, ArrayFloat input, Bool output);
```



**cumulativeDistribution(distributionv, limit, result)**

Compute the cumulative distribution. The distribution must be defined in the variable 'distributionv'. This functions sum the y-values of the distribution until to reach the limit that is compared to the x-values of the distribution. 

Types accepted:

```
cumulativeDistribution(DistributionInt distributionv, Int limit, Int result);
cumulativeDistribution(DistributionFloat distributionv, Float limit, Float result);
```



**max(array, max, index)**

Find the maximum value and its index in an array.

Types accepted:

```
max(ArrayInt array, Int value, Int index);
max(ArrayFloat array, Float value, Int index);
```


**min(array, max, index)**

Find the minimum value and its index in an array.

Types accepted:

```
min(ArrayInt array, Int value, Int index);
min(ArrayFloat array, Float value, Int index);
```


**mean(array, result)**

Compute the mean value of an array.

Types accepted:

```
mean(ArrayInt array, Int result);
mean(ArrayFloat array, Float result);
```


**sum(array, result)**

Sum all the elements of an array.

Types accepted:

```
sum(ArrayInt array, Int result);
sum(ArrayFloat array, Float result);
```


**energy(array, result)**

Compute the energy, defined as the sum of the squared values of an array.

Types accepted:

```
energy(ArrayInt array, Int result);
energy(ArrayFloat array, Float result);
```



**rms(array, result)**

Root means square.

Types accepted:

```
rms(ArrayInt array, Int result);
rms(ArrayFloat array, Float result);
```



**stdDev(array, result)**

Standard deviation.

Types accepted:

```
stdDev(ArrayInt array, Int result);
stdDev(ArrayFloat array, Float result);
```



**var(array, result)**

Variance.

Types accepted:

```
var(ArrayInt array, Int result);
var(ArrayFloat array, Float result);
```



**abs(input, output)**

Absolute value.

Types accepted:

```
abs(Int input, Int output);
abs(Float input, Float output);
abs(ArrayInt input, ArrayInt output);
abs(ArrayFloat input, ArrayFloat output);
```



**add(input1, input2, output)**

Add elements of an array (with a value) or add two arrays.

Types accepted:

```
add(ArrayInt input1, ArrayInt input2, ArrayInt output);
add(ArrayFloat input1, ArrayFloat input2, ArrayFloat output);
TODO: add with integer or float
```



**sub(input1, input2, output)**

Subtract elements of an array (with a value) or subtract of two arrays.

Types accepted:

```
sub(ArrayInt input1, ArrayInt input2, ArrayInt output);
sub(ArrayFloat input1, ArrayFloat input2, ArrayFloat output);
TODO: add with integer or float
```



**mult(input1, input2, output)**

Multiply elements of an array with a value or divide two arrays.

Types accepted:

```
mult(ArrayInt input1, Int input2, ArrayInt output);
mult(ArrayFloat input1, Float input2, ArrayFloat output);
mult(ArrayInt input1, ArrayInt input2, ArrayInt output);
mult(ArrayFloat input1, ArrayFloat input2, ArrayFloat output);
```



**div(input1, input2, output)**

Divide elements of an array with a value or divide two arrays.

Types accepted:

```
div(ArrayInt input1, Int input2, ArrayInt output);
div(ArrayFloat input1, Float input2, ArrayFloat output);
div(ArrayInt input1, ArrayInt input2, ArrayInt output);
div(ArrayFloat input1, ArrayFloat input2, ArrayFloat output);
```



**diff(input, output)**

Forward finite difference (derivative approximation).

Types accepted:

```
diff(ArrayInt input, ArrayInt output);
diff(ArrayFloat input, ArrayFloat output);
```



**dotProduct(input1, input2, output)**

Dot product of two arrays.

Types accepted:

```
dotProduct(ArrayInt input1, ArrayInt input2, ArrayInt output);
dotProduct(ArrayFloat input1, ArrayFloat input2, ArrayFloat output);
```



**negate(input, output)**

Negates each value of an array.

Types accepted:

```
negate(ArrayInt input, ArrayInt output);
negate(ArrayFloat input, ArrayFloat output);
```



**conv(input1, input2, output)**

Convolution between two array.

Types accepted:

```
conv(ArrayInt input1, ArrayInt input2, ArrayInt output);
conv(ArrayFloat input1, ArrayFloat input2, ArrayFloat output);
```



**corr(input1, input2, output)**

Correlation between two array.

Types accepted:

```
corr(ArrayInt input1, ArrayInt input2, ArrayInt output);
corr(ArrayFloat input1, ArrayFloat input2, ArrayFloat output);
```



**sqrt(input, output)**

Square root.

Types accepted:

```
sqrt(ArrayInt input, ArrayInt output);
sqrt(ArrayFloat input, ArrayFloat output);
```


**cos(input, output)**

Cosinus.

Types accepted:

```
cos(ArrayInt input, ArrayInt output);
cos(ArrayFloat input, ArrayFloat output);
```


**sin(input, output)**

Sinus.

Types accepted:

```
sin(ArrayInt input, ArrayInt output);
sin(ArrayFloat input, ArrayFloat output);
```



**log10(input, output)**

Common logarithm.

Types accepted:

```
log10(Float input, Float output);
log10(ArrayFloat input, ArrayFloat output);
```


**pow(input, power, output)**

Power of n.

Types accepted:

```
pow(Float input, Float power, Float output);
pow(ArrayFloat input, Float power, ArrayFloat output);
```


**magnitude(input, output)**

Magnitude of a complex number.

Types accepted:

```
magnitude(ArrayComplexInt input, ArrayInt output)
magnitude(ArrayComplexFloat input, ArrayFloat output)
```



### <a name="utility-functions-pane"></a> 6.4. _Utility functions_

**ascentRequest()**

Request the ascent of the float.

No parameters.



**getTimestamp(timestamp)**

Get the current date with a resolution of 1 second.

Types accepted:

```
getTimestamp(Int timestamp)
```



**record(file, data)**

Record a value, an array, a buffer or a string in a file.

Types accepted:

```
record(File file, Int data)
record(File file, Float data)
record(File file, ArrayInt data)
record(File file, ArrayFloat data)
record(File file, BufferInt data)
record(File file, BufferFloat data)
record(File file, String data)
```



**getSampleIndex(sampeIndex)**

Only for simulation. Get the index of current sample read in the input.

Types accepted:

```
getSampleIndex(Int sampeIndex)
```






<!--
All the functions accessibles to developpers are in a library...

To write an application, the user has access to a library. The library furnished with this example is very tiny. There are two kinds of concepts in the library: Sensors and Functions.

The library contains two sensors named `Range` and `Temperature` which are defined at beginning of the library in the `Sensor library` section. Each sensor has its own period and is associated with a reading function. In this demonstration, the periodic aspect of the sensors is simulated with Timer1 and Timer2 of the Arduino board. Later we will use separate concepts for Timers and Sensors, allowing us to use Timer and Sensors independently but also TimedSensors that is the concept used in this demonstration.

Warning: The range sensor sometimes returns an erroneous value of 0 for range that should be of several centimeters.

The library also contains several functions classified in different sections that are:

* `Math library` for mathematical functions and advanced variables handling like buffers.
* `Guard library` for functions that return boolean values used in conditional expressions.
* `System library` for system utilities specific to the instrument like recording functions or ascent requests.

Each function in the library is annotated with its execution time and the consumed energy.

The library file is in [MeLaLib/library.mela](MeLaLib/library.mela).
-->




## <a name="tutorial-pane"></a> 6. Tutorial

TODO

<!-- This tutorial a demonstration of the MeLa language to develop, analyze and compose applications. This is important to understand that this language is a very tiny prototype. Our interest is to have some user return about the usability and modularity of the language. Please feel free to send comments about the language or this tutorial by email to [bonnieux@i3s.unice.fr](bonnieux@i3s.unice.fr). -->



<!-- The tutorial shows how to develop and compose three applications, (1) an application that continuously reads the temperature sensor, (2) an application that detects when an object is near the range sensor, (3) an application that does the continuous recording of the range sensor. Although simple, these applications can be expanded to more complex real-life applications like CTD measurement for the temperature sensor or to a seismic detection algorithm for the range sensor. -->


<!-- ### <a name="app1-pane"></a> 6.1. app1

**Write application**

The first application continuously reads the temperature sensor and prints the values on the serial link.

Open the [MeLaApps/app1.mela](MeLaApps/app1.mela) file and set the park time to 100 minutes and the park depth to 50 meters.

Create an acquisition mode called "TemperatureRecord" that uses the temperature sensor.

```
mode TemperatureRecord with sensor Temperature:
endmode;
```

In the acquisition mode create a new sequence of instructions. The name of the sequence is completely independent of the acquisition mode name, so it's possible to use the same name.

```
sequence TemperatureRecord:
endseq;
```

Inside this sequence add the instruction to read the temperature sensor and store the value in a variable. You can use the specialized instruction `x = TemperatureDegCRead();` or the abstracted instruction `x = SensorRead();`. The storing variable must a `int16` globally declared as explained in [5.2](#global-var-pane).

Use the function `serialPrintInt32` and `serialPrintString` of the library to print on the serial link the value of `x` followed by the string `"degC\n"`.

In the mission configuration activate the `TemperatureRecord` during float's descent, park and ascent.

The resulting application can be found in [MeLaTuto/app1.mela](MeLaTuto/app1.mela).


**Run the java program**

Run the main in the [Compomaid.java](src/main/java/fr/uca/i3s/sparks/compomaid/Compomaid.java) file. An easy way to do this is to right click on the Compomaid file in the left pane (Project pane) of the IntelliJ editor.

The output of the console shows the different steps of execution of the Compomaid program.

First, the program transforms the MeLa library and the MeLa application into java models. The `Parse application` section shows all executable set of sequences called branches. In this simple application there is only one sequence:

```
********************************************
Parse application 1
********************************************
Branches of TemperatureRecord:
* TemperatureRecord

```


**Application analysis**

The analysis gives information about:

* Mission time with details for each step of the mission and the total time of a complete cycle.
* Errors in the scheduling of the application.
* Detailed energy consumption for each step of the mission and the instrument autonomy.

This is the result of analysis of the app1.

```
********************************************
Check application 1
********************************************
# Check mission configuration
* Descent during 27 min with acquisition modes:
  TemperatureRecord
* Park during 100 min at 50 m depth with acquisition modes:
  TemperatureRecord
* Ascent during 10 min with acquisition modes:
  TemperatureRecord
* Surface during 15 min
> Cycle time is 137 min

# Check application scheduling
* Check acquisition mode "TemperatureRecord":
  pass
> Scheduling check pass

# Check energy consumption
* Descent: 8 mWh
* Park: 11 mWh
* Ascent: 51.1 mWh
* Surface: 10 mWh
* Total: 80.1 mWh
> Float life time is 125000 cycles (32.58 years)
```

**Generated files**

After the application analysis, the program generates different files that can be found in the "generated" directory.

* The ".mela" file is the application written by the user but regenerated, this is particularly useful to see the result of a composition between two applications.
* The ".ino" file is the Arduino file that must be opened in the Arduino IDE to program the Arduino board.
* The "mission.cfg" file is a configuration file used by the instrument to configure the mission depth and time but this file is not used by the Arduino demonstration board.



### <a name="app2-pane"></a> 6.2. app2

**Write application part 1**

The second application uses the low range sensor to detect an object close to the demonstration board.

Open the app2.mela file and set the park time to 200 minutes and the park depth to 100 meters.

Create an acquisition mode that uses the `Range` sensor.

Create a sequence to read the sensor and use the `LevelUnder` function to detect if the range is under a value. The `LevelUnder` function is equal to the operator `x < y`, the first argument is the variable to compare, the second argument is the reference value and the returned value is a boolean equal to `true` if the comparison is valid. Save the returned value in a boolean variable and use this variable in a `if` expression. The annotation in the `if` expressions will depend of the estimated probability to enter in the condition that is determined by the probability to have `x < y`. If we consider that an object is near to the range sensor for 1 minute every hour, or 1/60*100 = 1.67%, the annotation will be then `@Probability = 1.67 pct`. Write the `goto` with the target sequence.

Complete the application with the sequence that prints the values on the serial link.

The corresponding application can be found [here](MeLaTuto/app2a.mela).

**Write application part 2**

Now we want to improve the low range detection and print only the lowest range that has been detected. For this, you can use a `Buffer` variable of size 20 that will contain the last 20 values of the buffer. Then use the `RisingEdgeDetection` to go to a `FindMin` sequence. The first instruction of this sequence will be the `bufferFindMin` function, the returned value has to be printed on the serial link.

Then use a third sequence that will contain the function `setAscentRequest();` which force the early ascent to surface of the instrument.

The `serialPrint` functions can also be replaced in the application by the `record` functions. For this latter function the content of the values are recorded in the EEPROM. The content of the EEPROM is printed on the serial link when the instrument is at the surface step, this can be added to an Iridium transmission for a real instrument.

The resulting application can be found [here](MeLaTuto/app2b.mela).


**Run the java program**

To parse the application 2 you must edit the [Compomaid.java](src/main/java/fr/uca/i3s/sparks/compomaid/Compomaid.java) file and set the `parse_app2` boolean equal to true.

As before the analysis will show you a mission description with each step, the result of the scheduling analysis and the estimated life time of the instrument.

The estimated park time is 60 minutes whereas the mission time specified by the user was 200 minutes. This is because in this example the estimated probability to do an early ascent was set to 1 per hour (50% of two low range detections per hour).

The scheduling analysis returns an error saying that 10 samples are missed each hour. This is due to the `bufferFindMin` function that use 500 ms of processing time whereas the sensor period is only 100 ms. Since the estimated execution period of this function is twice per hour, the total number of missed samples per hour is estimated to 10.

Also the probability to make an ascent request was set to 50% so the analysis say that 5 samples per hour are missed for the ascent request. They correspond to 50% of the 10 samples missed in the `FindMin` sequence.


```
# Check mission configuration
* Descent during 55 min
* Park during 60 min at 100 m depth with acquisition modes:
  LowRangeDetection
* Ascent during 20 min
* Surface during 15 min
> Cycle time is 135 min

# Check application scheduling
* Check acquisition mode "LowRangeDetection":
  ERROR for branch "DetectLowRange -> FindMin": 10 samples missed per hour
  ERROR for branch "DetectLowRange -> FindMin -> AscentRequest": 5 samples missed per hour
> Scheduling check FAIL

# Check energy consumption
* Descent: 10 mWh
* Park: 153.3 mWh
* Ascent: 100 mWh
* Surface: 10 mWh
* Total: 273.3 mWh
> Float life time is 36630 cycles (9.41 years)
```



### <a name="app1Uapp2-pane"></a> 6.3. app1 and app2 composition

Edit the [Compomaid.java](src/main/java/fr/uca/i3s/sparks/compomaid/Compomaid.java) file and set the `compose_app1_app2` boolean to true and run the program. This will cause to run the applications 1 and 2 together. In the generated directory you will find the app1Uapp2 application that is the result. For this simple method of composition, we consider that the app1 is the main application. Then we take the different acquisition mode of the app2 and put it inside the app1. As a result, the application has the mission configuration of the app1 and contains the acquisition modes of both applications that are `TemperatureRecord` (app1) and `LowRangeDetection` (app2).



### <a name="app3-pane"></a> 6.4. app3

Copy the content of the app1Uapp2.mela from the generated directory and paste the content into the MeLaApps/app1.mela file. In the app2.mela write a third application that continuously records the range sensor. The resulting application can be found [here](MeLaTuto/app3.mela).

Now compose the app1Uapp2 and app3 applications. The composed application still has the mission configuration of the app1 and now contain three acquisition modes that are `TemperatureRecord` (app1), `RangeRecord` (app3) and `LowRangeDetectionURangeRecord` (app2 and app3). The acquisition mode `LowRangeDetectionURangeRecord` is the composition of acquisition modes of app2 and app3 because they use the same sensor. The `RangeRecord` (app3 acquisition mode) is still in the application because it is executed independently during the descent and ascent step of the mission.

 -->