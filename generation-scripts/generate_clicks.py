import random
from sys import argv
from datetime import datetime, timedelta
from pathlib import Path

x_max = int(argv[1])
y_max = int(argv[2])
path = Path(__file__).parent / "../generated-data/input.txt"
rnd = random.Random()

with open(path, 'w') as file:
    for i in range(100):
        x = rnd.randint(0, x_max)
        y = rnd.randint(0, y_max)
        raw_timestamp = datetime.now()
        date = raw_timestamp - timedelta(hours=random.randint(0, 10))
        userid = rnd.randint(1, 10)
        file.write(f"{x},{y},{userid},{int(datetime.timestamp(date))}\n")
