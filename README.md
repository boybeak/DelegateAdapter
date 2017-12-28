[ ![DelegateAdapter](https://api.bintray.com/packages/boybeak/nulldreams/adapter/images/download.svg) ](https://bintray.com/boybeak/nulldreams/adapter/_latestVersion)
[ ![DelegateAdapter-Extension](https://api.bintray.com/packages/boybeak/nulldreams/adapter-extension/images/download.svg) ](https://bintray.com/boybeak/nulldreams/adapter-extension/_latestVersion)

# DelegateAdapter


This is an advanced RecyclerView's Adapter library. With this library, writing custom adapter class is not necessary in most conditions. Using DelegateAdapter can satisfy most of what you need.

The most amazing thing is binding multi types data and ViewHolder with Injections.

[Wiki](https://github.com/boybeak/DelegateAdapter/wiki)

[中文Readme入口](https://github.com/boybeak/DelegateAdapter/blob/master/README_CN.md)

[Selector](https://github.com/boybeak/DelegateAdapter/tree/master/selector)

# Download

Grab via Gradle:

```groovy
compile 'com.github.boybeak:adapter:3.1.5'
compile 'com.github.boybeak:adapter-extension:2.2.6' //Optional
```

# What's new in version 3.x.x

1. Reconstruct the library.
2. Add extension library.


You can read a full [release note](https://github.com/boybeak/DelegateAdapter/blob/master/ReleaseNote.md).

# Usage

[Typical Usage](https://github.com/boybeak/DelegateAdapter/wiki/3.-Typical-Usage)

[Advanced Usage](https://github.com/boybeak/DelegateAdapter/wiki/4.-Advanced-Usage)



## Adapter

The most important class is [DelegateAdapter](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/DelegateAdapter.java). With this adapter class, there's no need to make a your own custom adapter class.

```java
DelegateAdapter adapter = new DelegateAdapter (ActivityContext);
RecyclerView rv = ...;
//setLayoutManager etc;
rv.setAdapter(adapter);
```

### Add data

adapter add data like below:

```java
UserLayoutImpl userLayoutImpl = ...;
UserDelegateImpl userDelegateImple = ...;

User user = ...;
UserDelegate userDelegate = new UserDelegate (user);
UserAnnotationDeleagate annoDelegate = new UserAnnotationDelegate (user);
//create these User data or decode by gson from json
adapter.add (userLayoutImpl);
adapter.add (userDelegateImple);
adapter.add (userDelegate);
adapter.add (annoDelegate);
//Don't forget notifyDataSetChanged();
adapter.notifyDataSetChanged();
```

or you can add User model collection directly with a [DelegateParser](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/DelegateParser.java).

```java
List<User> userList = ...;//make this data your self, generally from json array
mAdapter.addAll(userList, new DelegateParser<User>() {
    @Override
    public LayoutImpl parse(DelegateAdapter adapter, User data) {
      	return new UserDelegate(data); //return a LayoutImpl or its sub class
    }
});
```

### Query Data

Query data with conditions

```java
//query count
int count = adapter.selector(UserDelegate.class)
                .where(Path.with(UserDelegate.class, Integer.class).methodWith("getSource").methodWith("getName").methodWith("length"), Operator.OPERATOR_GT, 4)
                .count();

//query names of users
List<String> names = adapter.selector(UserDelegate.class)
                .where(Path.with(UserDelegate.class, Integer.class).methodWith("getSource").methodWith("getName").methodWith("length"), Operator.OPERATOR_GT, 4)
                .extractAll(Path.with(UserDelegate.class, String.class).methodWith("getSource").methodWith("getName"));
```



## ViewHolder

all your ViewHolder classes should extend [AbsViewHolder](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/AbsViewHolder.java) class.

```java
public class UserHolder extends AbsViewHolder<UserDelegate> {

    private ImageView avatar;
    private TextView nameTv, descTv;

    public UserHolder(View itemView) {
        super(itemView);
        avatar = (ImageView)findViewById(R.id.avatar);
        nameTv = (TextView)findViewById(R.id.name);
        descTv = (TextView)findViewById(R.id.desc);
    }

    @Override
    public void onBindView(Context context, UserDelegate userDelegate, int position, DelegateAdapter adapter) {
        User user = userDelegate.getSource();
        avatar.setImageResource(user.getAvatar());
        nameTv.setText(user.getName() + " - " + getClass().getSimpleName());
        descTv.setText(user.getDescription());
    }
}
```

In this ViewHolder class you can bind data, bind event etc.

## DelegateParser, DelegateListParser

A serious of code snippets show you usage.

```java
/*DelegateParser*/
User[] users = ...; //Make your data yourself.
adapter.addAll (users, new DelegateParser<User>() {
    @Override
    public LayoutImpl parse(DelegateAdapter adapter, User data) {
      	return new UserDelegate(data); //return a LayoutImpl or its sub class
    }
});
```

```java
/*DelegateListParser*/
User[] users = ...; //Make your data yourself.
adapter.addAll (users, new DelegateListParser () {
    public List<LayoutImpl> parse (DelegateAdapter adapter, User data) {
		List<LayoutImpl> list = new ArrayList<LayoutImpl>();
      	list.add (new UserHeaderDelegate("I'm a good teacher"));
      	list.add (new UserDelegate (data));
      	if (data.isGood()) {
        	list.add(new UserFooterDelegate ("I am really a good teacher!"));
      	}
      	return list;
    }
});
```



## DataChange

[**DelegateAdapter** ](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/DelegateAdapter.java) returns a [DataChange](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/DataChange.java) instance after add, addAll and remove actions. You can call autoNotify directly.

```java
mAdapter.add(new TextDelegate("The Beatles")).autoNotify();
```

```java
mAdapter.remove(4).autoNotify();
```

```java
mAdapter.addAll(Data.getTwitterList(v), new DelegateParser<Twitter>() {
                    @Override
                    public DelegateImpl parse(DelegateAdapter adapter, Twitter data) {
                        return new TwitterDelegate(data);
                    }
                }).autoNotify();
```

## Item swap and delete

```java
mRv = (RecyclerView)findViewById(R.id.main_rv);

mAdapter = new TouchableAdapter(this);
mRv.setAdapter(mAdapter);

ItemTouchHelper helper = new ItemTouchHelper(new SimpleItemTouchHelperCallback(mAdapter,
                ItemTouchHelper.UP|ItemTouchHelper.DOWN, ItemTouchHelper.END));
helper.attachToRecyclerView(mRv);
```
