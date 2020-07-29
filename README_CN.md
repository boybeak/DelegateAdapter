[ ![Download](https://api.bintray.com/packages/boybeak/nulldreams/adapter/images/download.svg) ](https://bintray.com/boybeak/nulldreams/adapter/_latestVersion)
[ ![Download](https://api.bintray.com/packages/boybeak/nulldreams/adapter-extension/images/download.svg) ](https://bintray.com/boybeak/nulldreams/adapter-extension/_latestVersion)


# ~~DelegateAdapter~~ 
> Do not use this library ever. Try the more advanced one - [**AnyAdapter**](https://github.com/boybeak/AnyAdapter)

这是一个改进的RecyclerView框架下的适配Adapter库。使用这个库，在大多数情况下，你都不需要再写自定义的适配器Adapter类了。

[Wiki](https://github.com/boybeak/DelegateAdapter/wiki)

# Download

通过Gradle:

```groovy
compile 'com.github.boybeak:adapter:3.1.5'
compile 'com.github.boybeak:adapter-extension:2.2.6' //Optional
```



# What's new in version 3.x.x

1. 重构库结构。
2. 增加了扩展库。

# Usage

[典型用法](https://github.com/boybeak/DelegateAdapter/wiki/3.-Typical-Usage)

[先进用法](https://github.com/boybeak/DelegateAdapter/wiki/4.-Advanced-Usage)



## Adapter

[DelegateAdapter](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/DelegateAdapter.java) 这个类是该库中最为重要的一个类。使用这个适配器类，你基本就不用自定义适配器类了。

```java
DelegateAdapter adapter = new DelegateAdapter (ActivityContext);
RecyclerView rv = ...;
//setLayoutManager etc;
rv.setAdapter(adapter);
```

### 添加数据

如下方式添加数据:

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

或者你可以在 [DelegateParser](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/DelegateParser.java) 的配合下添加一组数据: 

```java
List<User> userList = ...;//make this data your self, generally from json array
mAdapter.addAll(userList, new DelegateParser<User>() {
    @Override
    public LayoutImpl parse(DelegateAdapter adapter, User data) {
      	return new UserDelegate(data); //return a LayoutImpl or its sub class
    }
});
```

### 查询数据

带条件的数据查询

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

你所有的 ViewHolder 类都应该继承自 [AbsViewHolder](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/AbsViewHolder.java) 类.

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

在这样一个ViewHolder类中，你可以进行绑定数据，绑定事件等操作。

## DelegateParser, DelegateListParser

接下来的几段代码展示一些较为便捷的操作。

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

```java
/*DelegateFilter*/
List<LayoutImpl> subUserDelegateList = adapter.getSubList (new DelegateAdapter() {
  	public boolean accept (DelegateAdapter adapter, LayoutImpl impl) {
      	return impl != null && impl instanceof UserDelegate;
  	}
});
```

```java
/*SimpleFilter*/
List<User> userList = adapter.getDataSourceArrayList (new SimpleFilter<User>());
```

## DataChange

[**DelegateAdapter** ](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/DelegateAdapter.java) 进行add, addAll, remove等操作后，返回了 [DataChange](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/DataChange.java) 实例, 如此便可以直接调用autoNofify方法，通知UI更新.

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
