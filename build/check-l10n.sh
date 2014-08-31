#!/bin/bash

echo
echo checking Project DataModel:
./check-l10n.pl ../src/de/cgarbs/knittr/resource/Project*

echo
echo checking MainWindow UI:
./check-l10n.pl ../src/de/cgarbs/knittr/resource/MainWindow*

echo
