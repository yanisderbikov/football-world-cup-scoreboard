# Football World Cup Scoreboard

Мини‑библиотека для отслеживания хода матчей ЧМ по футболу (старт, обновление счёта, завершение, сводка).


```java
Scoreboard board = new Scoreboard();
board.startGame("Mexico", "Canada");
board.updateScore("Mexico", "Canada", 0, 5);
board.getSummary().forEach(System.out::println);
```
