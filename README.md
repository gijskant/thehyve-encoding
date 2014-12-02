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
java -cp encoding-0.0.1.jar encoding.Read < test1.in >test1.out 2>test.err
```
The file `test1.in` contains `ab`, which is invalid input (there should be a `0` byte first).
It will output a stream of `\3f` characters:
```
od -An -tx1 < test1.in
od -An -tx1 < test1.out
od -An -tx1 < test1.err
```

The file `test2.in` was created with `printf '\000a\000b' > test2.in`. Run
```
java -cp encoding-0.0.1.jar encoding.Read < test2.in >test2.out 2>test2.err
```
and check the results in `test2.out` and `test2.err`:
```
od -An -tx1 < test2.in
od -An -tx1 < test2.out
od -An -tx1 < test2.err
```
