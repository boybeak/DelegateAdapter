[ ![Download](https://api.bintray.com/packages/boybeak/nulldreams/adapter/images/download.svg) ](https://bintray.com/boybeak/nulldreams/adapter/_latestVersion)

# DelegateAdapter
这是一个改进的RecyclerView框架下的适配Adapter库。使用这个库，在大多数情况下，你都不需要再写自定义的适配器Adapter类了。
# Download

通过Gradle:

```groovy
compile 'com.github.boybeak:adapter:2.2.0'
```

如果你需要使用selector操作，需要添加以下引用

```groovy
compile 'com.github.boybeak:selector:1.0.0'
```

# What's new in version 2.1.x

1. 通过[TouchableAdapter](https://github.com/boybeak/DelegateAdapter/tree/master/adapter/src/main/java/com/github/boybeak/adapter/touch/TouchableAdapter.java)与[SimpleItemTouchHelperCallback](https://github.com/boybeak/DelegateAdapter/tree/master/adapter/src/main/java/com/github/boybeak/adapter/touch/SimpleItemTouchHelperCallback.java)两个类, 增加了滑动删除与长按拖动切换. [详情参见](https://github.com/boybeak/DelegateAdapter/blob/master/README.md#item-swap-and-delete)

# Usage

我将分为4部分来介绍这个库的使用: Data, Adapter, ViewHolder and Advance Usages.

## Data

[DelegateAdapter](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/DelegateAdapter.java) 只接受 [LayoutImpl](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/impl/LayoutImpl.java) 类型的数据. 所以你的数据模型必须变成以下的两种类型:

1. 实现 [LayoutImpl](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/impl/LayoutImpl.java) 或者 [DelegateImpl](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/impl/DelegateImpl.java) 两个接口；
2. 使用实现了[LayoutImpl](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/impl/LayoutImpl.java) 接口的两个代理类 ([AbsDelegate](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/AbsDelegate.java), [AnnotationDelegate](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/annotation/AnnotationDelegate.java))。为了避免原始数据模型污染, 强烈建议你使用这种方式。

以下面这个数据模型为示例:

```java
public class User {
	private int avatar;
    private String name;
    private String description;
}
```

实现 [LayoutImpl](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/impl/LayoutImpl.java) 接口:

```java
public class UserLayoutImpl implements LayoutImpl{
	private int avatar;
    private String name;
    private String description;
  
	@Override
    public int getLayout() {
        return R.layout.layout_user;
    }

    @Override
    public Class<? extends AbsViewHolder> getHolderClass() {
        return UserHolder.class;
    }

    @Override
    public OnItemClickListener<LayoutImpl, AbsViewHolder> getOnItemClickListener() {
        return null;
    }

    @Override
    public OnItemLongClickListener<LayoutImpl, AbsViewHolder> getOnItemLongClickListener() {
        return null;
    }
  	@Override
    public int[] getOnClickIds() {
        return null;
    }
  	@Override
    public int[] getOnLongClickIds() {
        return null;
    }
}
```

实现 [DelegateImpl](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/impl/DelegateImpl.java) 接口:

```java
public class UserDelegateImpl implements DelegateImpl<UserDelegateImpl> {

    private int avatar;
    private String name;
    private String description;
  	
	@Override
    public UserDelegateImpl getSource() {
        return this;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_user;
    }

    @Override
    public Class<? extends AbsViewHolder> getHolderClass() {
        return UserHolder.clsas;
    }

    @Override
    public OnItemClickListener<LayoutImpl, AbsViewHolder> getOnItemClickListener() {
        return null;
    }

    @Override
    public OnItemLongClickListener<LayoutImpl, AbsViewHolder> getOnItemLongClickListener() {
        return null;
    }
  	@Override
    public int[] getOnClickIds() {
        return null;
    }
  	@Override
    public int[] getOnLongClickIds() {
        return null;
    }
}
```

继承代理类 [AbsDelegate](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/AbsDelegate.java):

```java
public class UserDelegate extends AbsDelegate<User> {
    
    private UserClickListener onClick = new UserClickListener();
    private UserLongClickListener onLongClick = new UserLongClickListener ();
    public UserDelegate(User user) {
        super(user);
    }

    @Override
    public int getLayout() {
        return R.layout.layout_user;
    }

    @Override
    public Class<? extends AbsViewHolder> getHolderClass() {
        return UserHolder.class;
    }

    @Override
    public OnItemClickListener<LayoutImpl, AbsViewHolder> getOnItemClickListener() {
        return onClick;
    }

    @Override
    public OnItemLongClickListener<LayoutImpl, AbsViewHolder> getOnItemLongClickListener() {
        return onLongClick;
    }
  	@Override
    public int[] getOnClickIds() {
        return null;
    }
  	@Override
    public int[] getOnLongClickIds() {
        return null;
    }
}
```

继承代理类 [AnnotationDelegate](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/annotation/AnnotationDelegate.java) 并使用注解:

```java
@DelegateInfo(
  	layoutID = R.layout.layout_user,
  	holderClass = UserHolder.class,
  	onClick = UserClickListener.class,
  	onLongClick = UserLongClickListener.class,
  	onClickIds = {DelegateAdapter.ITEM_VIEW_ID, R.id.user_avatar},
  	onLongClickIds = {DelegateAdapter.ITEM_VIEW_ID, R.id.user_avatar}
)
public class UserAnnotationDelegate extends AnnotationDelegate<User> {
  
    @OnClick(ids = {DelegateAdapter.ITEM_VIEW_ID, R.id.user_avatar})
    public Class<UserClickListener> onClick = UserClickListener.class;
  	@OnLongClick(ids = {DelegateAdapter.ITEM_VIEW_ID, R.id.user_avatar})
  	public Class<UserLongClickListener> onLongClick = UserLongClickListener.class;
  
    public UserDelegate(User user) {
        super(user);
    }
}
```



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

## Advance Usages

## 绑定事件

在新版本 1.2.0 中，你可以通过 **@DelegateInfo**, **@OnClick**, **@OnLongClick** 这些注解直接绑定点击，长按事件。

```java
@DelegateInfo(
  	layoutID = R.layout.layout_user,
  	holderClass = UserHolder.class,
  	onClick = UserClickListener.class,
  	onLongClick = UserLongClickListener.class,
  	onClickIds = {DelegateAdapter.ITEM_VIEW_ID, R.id.user_avatar},
  	onLongClickIds = {DelegateAdapter.ITEM_VIEW_ID, R.id.user_avatar}
)
public class UserAnnotationDelegate extends AnnotationDelegate<User> {
  
    @OnClick(ids = {DelegateAdapter.ITEM_VIEW_ID, R.id.text_tv})
    public Class<UserClickListener> onClick = UserClickListener.class;
  	@OnLongClick(ids = {DelegateAdapter.ITEM_VIEW_ID, R.id.text_tv})
  	public Class<UserLongClickListener> onLongClick = UserLongClickListener.class;
  
    public UserDelegate(User user) {
        super(user);
    }
}
```

> 如果你以及在 **@DelegateInfo** 注解中定义了**onClick** 或 **onLongClick** 属性 in , **@OnClick** 和 **@OnLongClick** 将不会起作用。

```java
public class UserClickListener implements OnItemClickListener<UserDelegate, UserHolder> {
    @Override
    public void onClick(View view, Context context, UserDelegate userDelegate, UserHolder userHolder, 
                        int position, DelegateAdapter adapter) {
        Toast.makeText(context, UserHolder.class.getSimpleName(), Toast.LENGTH_SHORT).show();
    }
}
```

```java
public class UserLongClickListener implements OnItemLongClickListener<UserDelegate, UserHolder> {
    @Override
    public boolean onLongClick(View view, Context context, UserDelegate userDelegate, UserHolder userHolder, 
                               int position, DelegateAdapter adapter) {
        new AlertDialog.Builder(context)
                .setMessage(userDelegate.getSource().getName())
                .show();
        return true;
    }
}
```



## DelegateParser, DelegateListParser, DelegateFilter, SimpleFilter

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

