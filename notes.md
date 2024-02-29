How to enable preview features in IDEA:
https://hanno.codes/2023/07/16/configuring-intellij-idea-to-try-out-javas-preview-features/#:~:text=per%2Dmodule%20basis.-,Enabling%20Preview%20Features%20Per%2DModule,compiler%20parameters%20per%2Dmodule'.

In Gradle
```groovy
tasks.withType(JavaCompile).configureEach {
    options.compilerArgs += "--enable-preview"
}
```


## GlobalLockModel

### Version 1

GlobalLockModel - Cells: 10, Particles: 8, TransitionFactor: 0.5, Duration: 5 = Average transitions: 38073305
