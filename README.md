thehyve-encoding
================

Stream encoding exercise

Build
---------------
Type `mvn package`

Run
---------------
In `target`, run, e.g., the following commands:

```
java -cp encoding-0.0.1.jar encoding.Read < test1.in
```
It will give an error, as `test1.in` contains `ab`, without the required `0` byte.

The file `test2.in` was created with `printf '\000a\000b' > test2.in`. Run
```
java -cp encoding-0.0.1.jar encoding.Read < test2.in >test2.out 2>test2.err
```
and check the results in `test2.out` and `test2.err`:
```
echo "in:"
od -An -tx1 < test2.in
echo "out:"
od -An -tx1 < test2.out
echo "err:"
od -An -tx1 < test2.err
```
