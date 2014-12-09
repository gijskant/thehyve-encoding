#!/bin/bash

s=("a" "b" "c" "c" "a" "b" "a" "b" "c")

if [ -e test.in ]
then
  rm test.in
fi
if [ -e test.exp ]
then
  rm test.exp
fi
for i in {1..1000}
do
  for c in ${s[@]}
  do
    printf "\000${c}" >> test.in
    printf "${c}" >> test.exp
  done
done
#printf '\n' >> test.in
#printf '\n' >> test.exp

od -An -tx1 < test.in >test.in.txt

java -cp target/encoding-0.0.1.jar encoding.Read < test.in >test.out 2>test.err

od -An -tx1 < test.out > test.out.txt
od -An -tx1 < test.err > test.err.txt

# Reencoding the output of the first run should yield the same output
# as the first run.
java -cp target/encoding-0.0.1.jar encoding.Read < test.err >test2.out 2>test2.err

od -An -tx1 < test2.out > test2.out.txt
od -An -tx1 < test2.err > test2.err.txt

