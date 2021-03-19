from sys import argv
from math import sqrt

x = int(argv[1])
y = int(argv[2])
areas_number = int(argv[3])

axis_areas_number = sqrt(areas_number)

x_step = int(x / axis_areas_number)

y_step = int(y / axis_areas_number)

with open("../generated-data/areas.txt", "w") as file:
    for x_ptr in range(0, x, x_step):
        for y_ptr in range(0, y, y_step):
            file.write(f"{x_ptr},{x_ptr + x_step},{y_ptr},{y_ptr + y_step}\n")
