# DelegateAdapter
An advanced RecyclerView's Adapter.There is no need to custom an Adapter if you use this library.
This library binds layout file, data and ViewHolder with Annotation.

<img src="https://github.com/boybeak/DelegateAdapter/blob/master/showcase.png" width="360" height="640"/>
## How to use.
for gradle with jCenter user:
```
compile 'com.github.boybeak:adapter:1.1.0'
```


#### AnnotationDelegate的使用方法
一般来说，使用DelegateAdapter配合AnnotationDelegate就可以满足大部分需求。
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

#### DelegateAdapter的使用方法
```
DelegateAdapter adapter = new DelegateAdapter(context);
```
添加数据
```
User user = new User(R.drawable.img1, "Jack", "Jack slow f**k");
adapter.add (new UserDelegate(user));
adapter.notifyDataSetChanged();
```
如果DelegateAdapter只与AnnotationDelegate配合使用，以上使用方法就可以满足；
如果还配合非AnnotationDelegate的AbsDelegate使用，则DelegateAdapter必须重写onHolderClassNotFound方法，并删除super.onHolderNotFound.
并提供对应的AbsViewHolder.

#### 不使用注解，可以直接使用AbsDelegate
在AnnotationDelegate中，使用了反射来获取注解中的值，所以性能会稍微受到影响，如果想提高效率，可以直接继承AbsDelegate，在抽象方法getLayout与getHolderClass中去提供对应的布局与holder类。
当然，对于继承自AbsDelegate的类中，也可以使用注解，只是未对注解中对值做缓存，可能造成多次反射的问题。

#### 使用LayoutImpl
如果你不想使用AbsDelegate，也可以使用LayoutImpl,例如：
```
public class User implements LayoutImpl {
    public int getLayout () {
        return R.layout.layout_user;
    }
}
```
> 建议使用继承AnnotationDelegate的方法来做，在AnnotationDelegate中以及做了缓存layout与holderClass，不需要多次反射获取

#### 其他使用方法
之所以提倡使用Delegate的方式是为了避免污染原数据，例如界面列表的选中状态就不该存储与数据Model中。
如果你不介意数据污染，可以让原数据类实现LayoutImpl接口，同样要给出DelegateInfo活着是LayoutID,HolderClass注解
#### 其他便捷方法
DelegateAdapter中封装了一些便利方法，例如：
addIfNotExist，addAll，addAllAtFirst， addAllAtLast， getDataSourceArrayList，
getSubList， firstIndexOf， replaceWith等。

#### 联系我
也许我只是重复造了个轮子，期待一起改善。
新浪微博:没洗干净的葱
QQ:915522070
