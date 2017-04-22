# Boolean Expression Simplifier and Visualizer

   The application takes 2 to 4 inputed boolean expression or truth table as input and simplifies it using Quine-Mccluskey method and shows the
    K-Map simplification way with colors.

    *Input can be entered to GUI by:
        +Typing to text field as: 
            A.B'.C + A.B.C' etc. (Sum of product form)
        +Generating truth table with size 2,3 or 4 and editing it

    *Input can be imported as file:
        +Creating .be extension file written as
            A.B.C + A'.B'.C
        +Creating .tt extension file written as
            A,B;F
            0,0;0
            0,1;1
            1,0;1
            1,1;0

    *Extra features:
        +File imports
        +Chaning theme

    *Known bugs:
        +Cannot solve for not complete forms such as: A.B + B'.C
        +Not checking for 1 inputs

