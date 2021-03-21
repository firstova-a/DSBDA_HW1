#!/bin/sh

X_MAX=20
Y_MAX=20
AREAS_NUMBER=4

if [ -d "generated-data/" ]
    mkdir generated-data/
fi

python3 generation-scripts/generate_areas.py $X_MAX $Y_MAX $AREAS_NUMBER
python3 generation-scripts/generate_clicks.py $X_MAX $Y_MAX
