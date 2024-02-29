How to enable preview features in IDEA:
https://hanno.codes/2023/07/16/configuring-intellij-idea-to-try-out-javas-preview-features/#:~:text=per%2Dmodule%20basis.-,Enabling%20Preview%20Features%20Per%2DModule,compiler%20parameters%20per%2Dmodule'.

In Gradle
```groovy
tasks.withType(JavaCompile).configureEach {
    options.compilerArgs += "--enable-preview"
}
```


## GlobalLockModel (15 iterations)

GlobalLockModel - Cells: 10, Particles: 8, TransitionFactor: 0.5, Duration: 5 = Average transitions: 38073305

### Optimization 1

Thread.interrupted() changed to volatile variable and Random changed to ThreadLocalRandom:  increase 41%

GlobalLockModel - Cells: 10, Particles: 8, TransitionFactor: 0.5, Duration: 5 = Average transitions: 65148690

### Optimization 2

Use array instead of AtomicLong: increase  22%

GlobalLockModel - Cells: 10, Particles: 8, TransitionFactor: 0.5, Duration: 5 = Average transitions: 88064151

### Optimization 3 

Use ReentrantLock: increase 32%

GlobalLockModel - Cells: 10, Particles: 8, TransitionFactor: 0.5, Duration: 5 = Average transitions: 124206557

## LockPerCellModel (15 iterations)

ReentrantLock:

LockPerCellModel - Cells: 10, Particles: 8, TransitionFactor: 0.5, Duration: 5 = Average transitions: 39171087
LockPerCellModel - Cells: 16, Particles: 8, TransitionFactor: 0.5, Duration: 5 = Average transitions: 40780903
LockPerCellModel - Cells: 32, Particles: 8, TransitionFactor: 0.5, Duration: 5 = Average transitions: 60171669
LockPerCellModel - Cells: 64, Particles: 8, TransitionFactor: 0.5, Duration: 5 = Average transitions: 92198416
LockPerCellModel - Cells: 128, Particles: 8, TransitionFactor: 0.5, Duration: 5 = Average transitions: 147791439

### Improvement 1

Use synchronized: increase 32%, 27%

LockPerCellModel - Cells: 16, Particles: 8, TransitionFactor: 0.5, Duration: 5 = Average transitions: 56603538
LockPerCellModel - Cells: 128, Particles: 8, TransitionFactor: 0.5, Duration: 5 = Average transitions: 202498253
