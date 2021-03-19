import random
from datetime import datetime, timedelta

rnd = random.Random()

with open('../generated-data/input.txt', 'w') as file:
    for i in range(100):
        x = rnd.randint(0, 20)
        y = rnd.randint(0, 20)
        raw_timestamp = datetime.now()
        date = raw_timestamp - timedelta(hours=random.randint(0, 10))
        userid = rnd.randint(1, 10)
        file.write(f"{x},{y},{userid},{int(datetime.timestamp(date))}\n")
