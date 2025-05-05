#!/bin/sh
stagedFiles=$(git diff --staged --name-only)

mvn spotless:apply --non-recursive || exit 1

for file in $stagedFiles; do
  if test -f "$file"; then
    git add $file
  fi
done
