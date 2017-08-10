# Selector

This is a very simple library for querying or modifying a List. You can query or modify a list with sql style.

## Install

```groovy
compile 'com.github.boybeak:selector:0.3.4'
```

## Usage
**Start with version 0.2.0, you can handle an array with Selector.**

With a List
```java
List list = new ArrayList<>();
//add some data different type.

//get count of user.getName.length > 5
int count = Selector.selector(User.class, list)
                .where(Path.with(User.class, Integer.class).methodWith("getName.length"), Operator.OPERATOR_GT, 5)
  				.count();

//get all user of user.getName.length > 5
List<User> userList = Selector.selector(User.class, list)
                .where(Path.with(User.class, Integer.class).methodWith("getName.length"), Operator.OPERATOR_GT, 5)
  				.findAll();

//get all user.name of user.getName.length > 5
List<String> nameList = Selector.selector(User.class, list)
                .where(Path.with(User.class, Integer.class).methodWith("getName.length"), Operator.OPERATOR_GT, 5)
                .extractAll(Path.with(User.class, String.class).methodWith("getName"));

//travle with miltiple conditions
Selector.selector(User.class, list).where(Path.with(User.class, Integer.class).methodWith("getName").methodWith("length"), Operator.OPERATOR_GT, 5)
                .and(Path.with(User.class, String.class).methodWith("getAvatar"), Operator.OPERATOR_IS_NULL).map(new Action<User>() {
            @Override
            public void action(int index, User user) {
                user.setDescription("This is a name length > 5 and null avatar user");
            }
        });
```