#!/bin/bash

# Count commits by each contributor
git log --pretty="%an" | sort | uniq -c | sort -nr
