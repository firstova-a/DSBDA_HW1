#!/bin/sh

X_MAX=20
Y_MAX=20
AREAS_NUMBER=4

mkdir generated-data/ || true
python3 generation-scripts/generate_areas.py $X_MAX $Y_MAX $AREAS_NUMBER
python3 generation-scripts/generate_clicks.py $X_MAX $Y_MAX
