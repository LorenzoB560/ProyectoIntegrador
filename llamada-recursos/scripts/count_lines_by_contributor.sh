#!/bin/bash

# Count lines added/removed by each contributor for .java files under src/main and src/test
for author in $(git log --format='%aN' | sort -u); do
  echo "$author"
  git log --author="$author" --pretty=tformat: --numstat -- 'src/main/**/*.java' 'src/test/**/*.java' |
    awk '{ add += $1; del += $2 } END { printf "  Added: %s, Removed: %s, Net: %s\n", add, del, add-del }'
done
