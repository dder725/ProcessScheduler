# SoftEng306: Project 1 -- ScheME Process Scheduler
<p align="center">
  <img src="schemeWhite.png">
</p>

Implementation of branch-and-bound type algorithm that solves process scheduling problem optimally for small input graphs.

The wiki contains the algorithm research and implementations Our project plan can be found in the "groupPlans" folder. Our final jar file is available under the newStartTime branch on this repository


## Group 9

| Student        | UPI           | GitHub Username  |
| ---------------|:-------------:| ----------------:|
| Jiaqiu Zhao    | jzha653       |   jzha653        |
| Daria Derecha  | dder725       |   dder725        |
| Guangya Zhu    | gzhu282       |   NoMacAlive     |
| Brian Zhang    | bzha841       |   brianzhang310  |
| Lisa Wang      |zwan940        |  zwan940         |

## Getting started
To be added

## Running the program
From the directory where the .jar file is located, run the following command:
```
java −jar scheduler.jar INPUT.dot P [OPTION]
```
* INPUT.dot is a task graph to be processed 
* P is the number of processors to schedule the input graph on

### Optional:
* -p N  -- use N cores for execution in parallel (default is sequential)
* -v    -- visualize the search
* -o OUTPUT -- OUTPUT is the name of the output .dot file
* -a    --  switch the algorithm to Branch and Bound

## Note:
* The input dot file has to be placed next to the jar file
