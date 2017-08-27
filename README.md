[ ![Download](https://api.bintray.com/packages/boybeak/nulldreams/adapter/images/download.svg) ](https://bintray.com/boybeak/nulldreams/adapter/_latestVersion)

# DelegateAdapter


This is an advanced RecyclerView's Adapter library. With this library, writing custom adapter class is not necessary in most conditions. Using DelegateAdapter can satisfy most of what you need.

The most amazing thing is binding multi types data and ViewHolder with Injections.

[中文Readme入口](https://github.com/boybeak/DelegateAdapter/blob/master/README_CN.md)

[Selector](https://github.com/boybeak/DelegateAdapter/tree/master/selector)

# Download

Grab via Gradle:

```groovy
compile 'com.github.boybeak:adapter:2.3.1'
```

# What's new in version 2.3.x

1. [DelegateAdapter](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/github/boybeak/adapter/DelegateAdapter.java) supports single and multiple selection.([See Details](#Choose mode))


You can read a full [release note](https://github.com/boybeak/DelegateAdapter/blob/master/ReleaseNote.md).

# Usage

I will introduce this library by 4 parts: Data, Adapter, ViewHolder and Advance Usages.

## Data

[DelegateAdapter](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/DelegateAdapter.java) only accept [LayoutImpl](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/impl/LayoutImpl.java) data. So your data model must be changed into one of the two types as below:

1. implements [LayoutImpl](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/impl/LayoutImpl.java) or [DelegateImpl](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/impl/DelegateImpl.java);
2. with a delegate class extends LayoutImpl's sub classes ([AbsDelegate](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/AbsDelegate.java), [AnnotationDelegate](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/annotation/AnnotationDelegate.java)). In case of original data model pollution, I strongly suggest you use this.

An example class as below:

```java
public class User {
	private int avatar;
    private String name;
    private String description;
}
```

Implements [LayoutImpl](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/impl/LayoutImpl.java):

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

implements [DelegateImpl](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/impl/DelegateImpl.java):

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

with a delegate class extends [AbsDelegate](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/AbsDelegate.java):

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
}
```

with a delegate class extend [AnnotationDelegate](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/annotation/AnnotationDelegate.java) and injections:

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
  	@OnLongClick(ids = {DelegateAdapter.ITEM_VIEW_ID, R.id.text_tv})
  	public Class<UserLongClickListener> onLongClick = UserLongClickListener.class;
  
    public UserDelegate(User user) {
        super(user);
    }
}
```

If you define the same type attribute both in **@DelegateInfo** and class member variables, only the **@DelegateInfo** will work.



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

## Advance Usages

## Bind event

With version 1.2.0, you can bind itemView click and longClick event via **@DelegateInfo**, **@OnClick**, **@OnLongClick** injections.

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

> If you define **onClick** and **onLongClick** attribute in **@DelegateInfo**, **@OnClick** and **@OnLongClick** will not work. This principles also to their inner attributes.

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

## Choose mode

Your data or delegate item must implements Checkable.

for single choose:

```java
adapter.singleControl();
```

for multiple choose:

```java
adapter.multipleControl();
```

After this, the DelegateAdapter is **underControl** mode. And this method returns Controller instance. You can set some callbacks for this instance. 

```java
adapter.dismissControl();
```

Exit the choose mode.