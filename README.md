# Convex-Hull
Abstract:
In this project, Java Swing is used to develop convex hull techniques such as Jarvis March, Graham Scan, Brute Force, Quick Elimination, and Incremental approaches. In addition, the Brute Force, Sweep Line, and Cross Product techniques are used to verify line intersections. The project's goal is to evaluate these algorithms' performance and efficiency. 

Introduction:
Convex hull algorithms—which identify the smallest convex polygon enclosing a set of points—and line intersection algorithms—which ascertain whether two lines intersect—are the main topics of this study. While line intersection techniques find applications in a variety of domains, such as robotics and computer-aided design, convex hull algorithms find extensive use in computational geometry and computer graphics. 
Programming design Programming Language: 
Programming language used is Java.

![image](https://github.com/HairamNaseem/Convex-Hull/assets/123382738/789fb429-a8a1-46cd-becf-a3de5738f5e6)

Experimental Setup:
Hardware used was core i7 Laptop with Nvidia graphics card.
software used was IntelliJ version 2023.2.5 on windows 11. 
![image](https://github.com/HairamNaseem/Convex-Hull/assets/123382738/f2aad7c9-ffc3-4e94-adc9-55a1387ee60e)

Conclusion: 
a. Brute Force: 
Strengths: Conceptually simple, applicable to any polygon. 
Weaknesses: Inefficient for large datasets, time complexity is O(n ^4 ). 
b. Jarvis March: 
Strengths: Simple to implement, works well for a small number of points. 
Weaknesses: Inefficient for large datasets, time complexity is O(nh), where ℎ h is the number of points on the convex hull. 
c. Graham Scan: 
Strengths: Efficient for a moderate number of points, time complexity is O(nlogn).
 Weaknesses: Not suitable for collinear points, implementation can be complex. 
d. Quick Elimination: 
Strengths: Efficient for large datasets, time complexity is O(nlogn). 
Weaknesses: Limited to specific cases, not always applicable. 
e. Incremental: 
Strengths: Adaptable to dynamic datasets, efficient for incremental updates. 
Weaknesses: Can be more complex to implement, might not be as efficient for static datasets.
For general convex hull problems, the Graham Scan algorithm is often preferred due to its efficiency for moderate-sized datasets and widespread use in various applications.
References:
Introduction to design and analysis of algorithms by Anany Levitin
Introduction to algorithms by Thomas H. Cormen
