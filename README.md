# DelegateAdapter
An advanced RecyclerView's Adapter

## How to use.
#### AnnotationDelegate的使用方法
一般来说，使用AnnotationAdapter配合AnnotationDelegate就可以满足大部分需求。
```
@DelegateInfo(layoutID = R.layout.layout_user, holderClass = UserHolder.class)
public class UserDelegate extends AnnotationDelegate<User> {
    public UserDelegate(User user) {
        super(user);
    }
}
```
注意注解@DelegateInfo，指定了其使用的布局layout id与对应的ViewHolder的class.
由于在library project中使用常量的限制，在library project中不能这样使用。可以使用另外一种方式。
```
public class UserDelegate extends AnnotationDelegate<User> {
    @LayoutID
    public int layoutId = R.layout.layout_user;
    @HolderClass
    public Class<? extends AbsViewHolder> holderClass = UserHolder.class;
    public UserDelegate(User user) {
        super(user);
    }
}
```
注意其中变量的注解@LayoutID, @HolderClass.

#### AnnotationAdapter的使用方法
···
AnnotationAdapter adapter = new AnnotationAdapter(this);
···
添加数据
```
User user = new User(R.drawable.img1, "Jack", "Jack slow f**k");
adapter.add (new UserDelegate(user));
adapter.notifyDataSetChanged();
```
如果AnnotationAdapter只与AnnotationDelegate配合使用，以上使用方法就可以满足；
如果还配合非AnnotationDelegate的AbsDelegate使用，则AnnotationAdapter必须重写onHolderNotFound方法，并删除super.onHolderNotFound.
并提供对应的AbsViewHolder.

#### 其他用法
由于使用注解的形式，采用反射的形式去获取布局以及ViewHolder信息，性能会有一定的损失，如果你对性能要求极高，可以使用普通的DelegateAdapter与AbsDelegate.
```
public class DemoAdapter extends DelegateAdapter {
    .............

    public AbsViewHolder onCreateAbsViewHolder (ViewGroup parent, int viewType, View itemView) {
        switch (viewType) {
            case ...:
                return ...;
        }
    }

}
```
此时的Delegate类也不该继承AnnotationDelegate,而是继承AbsDelegate.
```
public class UserDelegate extends AbsDelegate<User> {
    public UserDelegate (User user) {
        super(user);
    }

    public int getLayout() {
        return R.layout.layout_user;
    }
}
```

#### 使用DelegateImpl
如果你不想使用AbsDelegate，也可以使用DelegateImpl,例如：
```
public class User implements DelegateImpl {
    public int getLayout () {
        return R.layout.layout_user;
    }
    public int getType () {
        return type;
    }
}
```
> AbsDelegate中，getType与getLayout返回值相同,都是layout id。

#### 其他便捷方法
DelegateAdapter中封装了一些便利方法，例如：
addIfNotExist，addAll，addAllAtFirst， addAllAtLast， getDataSourceArrayList，
getSubList， firstIndexOf， replaceWith等。

#### 联系我
也许我只是重复造了个轮子，期待一起改善。
新浪微博:没洗干净的葱
QQ:915522070