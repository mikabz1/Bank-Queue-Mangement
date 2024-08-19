# Project summary

This project is a work for a bachelor's degree in computer science in the object-oriented Java course.

The project comes to test and create skills in the following subjects:

-Object-oriented design including multiple classes.

-data structure.

-Programming in Java language.

-Multithreading programing.

-Synchronized thread.

-problem solving.

-debugging.

The project is a simulation of a bank queue

The simulation includes the bank tellers, the customers and the bank clock and the program sampler.

The goal is that every time a customer arrives, one of the available tellers, if there are any, will "take" him and give him service for a certain amount of time.

# Bank:
Activates a clock that will notify when the working day is over.

•⁠  ⁠Creates tellers according to the number needed and starts their operation.

•⁠  ⁠A sampler who will demonstrate the system.

•⁠  ⁠Creates customers according to a predetermined rate and starts the operation of each of them.

The length of the working day, the number of tellers who work in it, the rate of arrival of customers and sampling cycle,
the queues are all parameters set in advance when creating the bank.

# Clock:

Determines the framework of the working day: he must announce when the bank's activity begins and when it ends.

# Teller

The teller receives customers from the queue at the bank in order, and treats each one accordingly to the time needed for the customer.

•⁠  ⁠The teller takes the customer at the top of the queue and takes care of him (for as long as the customer needs - information).

•⁠⁠ When finished, invites the next customer.

•⁠  ⁠The teller works all day without breaks.

•⁠  ⁠At the end of the working day, the teller must stop working, not before the queue of customers was emptied.

•⁠  ⁠The teller informs (prints) at the end of the day how many customers he handled.

# Customer:

A customer who sometimes comes to the bank to receive service from a teller.

The customer waits in queue, receives service. 

The customer has three modes:

•⁠  ⁠Waiting 

•⁠  ⁠Receives service 

•⁠   Done

The customer's service time is known to him in advance, and this is the time he spends in the state of "receiving service"

# Sampler

Samples the system according to a predetermined rate and prints information.

The sampler wakes up every time to sample the system and prints the information about it (for example, which teller serves which customer, etc.). 

The sampling rate predetermined when creating the sampler.


Project UML:


![9DC17598-144A-4DFC-AB70-2C832B93AA91_1_201_a](https://github.com/user-attachments/assets/f59be04a-48c9-4a7c-b85f-28d70d1f56d4)

# Challenges during the project:

Managing the waking and sleeping of each thread.

Multiple threads access a shared resource.

Synchronization between different threads linked together.
