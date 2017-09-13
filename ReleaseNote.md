**3.0.0**

1. Reconstruct this library.
2. Add extension library.

**2.3.0**

1. Support choose mode:single choose and multiple choose.

**2.2.0**

1. [DelegateAdapter](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/github/boybeak/adapter/DelegateAdapter.java) can hold some states via bundle() method, such as checkable, selectable etc.

**2.1.0**

1. Add item swipe delete and long press drag swap with 2 classes, [TouchableAdapter](https://github.com/boybeak/DelegateAdapter/tree/master/adapter/src/main/java/com/github/boybeak/adapter/touch/TouchableAdapter.java) and [SimpleItemTouchHelperCallback](https://github.com/boybeak/DelegateAdapter/tree/master/adapter/src/main/java/com/github/boybeak/adapter/touch/SimpleItemTouchHelperCallback.java).

**2.0.x**

1. Add a very strong way to query data with conditions from DelegateAdapter with the library [Selector](https://github.com/boybeak/DelegateAdapter/tree/master/selector).
2. Fix some bugs.

**1.4.x**

1. Add [DataChange](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/DataChange.java) class with a method **autoNotify**. This represents **notifyItemInserted**, **notifyItemRangeInserted**, **notifyItemChanged** and so on. After DelegateAdapter add, addAll or remove methods invoked, return a DataChange instance.

**1.3.x**

1. [AbsDelegate](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/AbsDelegate.java) can bring parameters with using a Bundle.
2. Click and long click event now also works on ItemView's child views. You can define **onClickIds** and **onLongClickIds** attribute in **@DelegateInfo** injection, or alternative define **ids** attribute in **@OnClick** and **@OnLongClick**.

**1.2.x**

1. Add click and long click event to ItemView.
2. **@LayoutInfo** annotation now can work with **@LayoutID**, **@HolderClass**, **@OnClick**, **@OnLongClick** together.
