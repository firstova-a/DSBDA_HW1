#!/bin/sh

X_MAX=20
Y_MAX=20
AREAS_NUMBER=4

if [ ! -d "generated-data/" ]
then
    mkdir generated-data/
fi

echo "[+] Generating areas file generated_data/areas.txt"
python3 generation-scripts/generate_areas.py $X_MAX $Y_MAX $AREAS_NUMBER

echo "[+] Generating clicks file generated_data/input.txt"
python3 generation-scripts/generate_clicks.py $X_MAX $Y_MAX
