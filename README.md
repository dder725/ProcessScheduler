# SoftEng306: Project 1 -- ScheME Process Scheduler
![Product Logo](schemeSmall.png)

Implementation of branch-and-bound type algorithm that solves process scheduling problem optimally for small input graphs.
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
