#DelegateAdapter
This is an advanced RecyclerView's Adapter library. With this library, writing custom adapter class is not necessary in most conditions. Using DelegateAdapter can satisfy most of what you need.

The most amazing thing is binding multi types data and ViewHolder with Injections.

#Download
Grab via Meven:

```xml
<dependency>
  <groupId>com.github.boybeak</groupId>
  <artifactId>adapter</artifactId>
  <version>1.2.0</version>
  <type>pom</type>
</dependency>
```

or Gradle:

```groovy
compile 'com.github.boybeak:adapter:1.2.0'
```

This library requires minSdkVersion 15(Don't ask me why, I just dislike previous versions).

# What's new in version 1.2.0

1. add click and long click event support;
2. LayoutInfo annotation now can work with LayoutID, HolderClass, OnClick, OnLongClick together.



# Usage

I will introduce this library by 4 parts: Data, Adapter, ViewHolder and Advance Usages.

## Data

[DelegateAdapter](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/DelegateAdapter.java) only accept [LayoutImpl](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/impl/LayoutImpl.java) data. So your data model must be changed into one of the two types as below:

1. implements LayoutImpl or DelegateImpl;
2. with a delegate class extends LayoutImpl's sub classes ([AbsDelegate](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/AbsDelegate.java), [AnnotationDelegate](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/annotation/AnnotationDelegate.java)).

An example class as below:

```java
public class User {
	private int avatar;
    private String name;
    private String description;
}
```

Implements LayoutImpl:

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
}
```

implements DelegateImpl:

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
}
```

with a delegate class extends AbsDelegate:

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

with a delegate class extend AnnotationDelegate and injections:

```java
@DelegateInfo(
  	layoutID = R.layout.layout_user,
  	holderClass = UserHolder.class,
  	onClick = UserClickListener.class,
  	onLongClick = UserLongClickListener.class
)
public class UserAnnotationDelegate extends AnnotationDelegate<User> {
  
    @OnClick
    public Class<UserClickListener> onClick = UserClickListener.class;
  	@OnLongClick
  	public Class<UserLongClickListener> onLongClick = UserLongClickListener.class;
  
    public UserDelegate(User user) {
        super(user);
    }
}
```



## Adapter

The most important class is [DelegateAdapter](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/DelegateAdapter.java). With this adapter class, there's no need to make a your own custom adapter class.

```java
DelegateAdapter adapter = new DelegateAdapter (ActivityContext);
RecyclerView rv = ...;
//setLayoutManager etc;
rv.setAdapter(adapter);
```

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
  	onLongClick = UserLongClickListener.class
)
public class UserAnnotationDelegate extends AnnotationDelegate<User> {
  
    @OnClick
    public Class<UserClickListener> onClick = UserClickListener.class;
  	@OnLongClick
  	public Class<UserLongClickListener> onLongClick = UserLongClickListener.class;
  
    public UserDelegate(User user) {
        super(user);
    }
}
```

> If you define **onClick** and **onLongClick** attribute in **@DelegateInfo**, **@OnClick** and **@OnLongClick** will not work.

```java
public class UserClickListener implements OnItemClickListener<UserDelegate, UserHolder> {
    @Override
    public void onClick(View view, Context context, UserDelegate userDelegate, UserHolder userHolder, int position, DelegateAdapter adapter) {
        Toast.makeText(context, UserHolder.class.getSimpleName(), Toast.LENGTH_SHORT).show();
    }
}
```

```java
public class UserLongClickListener implements OnItemLongClickListener<UserDelegate, UserHolder> {
    @Override
    public boolean onLongClick(View view, Context context, UserDelegate userDelegate, UserHolder userHolder, int position, DelegateAdapter adapter) {
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

