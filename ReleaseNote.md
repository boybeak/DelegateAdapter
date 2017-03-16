**1.2.0**

1. Add click and long click event to ItemView.
2. **@LayoutInfo** annotation now can work with **@LayoutID**, **@HolderClass**, **@OnClick**, **@OnLongClick** together.

**1.3.0**

1. [AbsDelegate](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/AbsDelegate.java) can bring parameters with using a Bundle.
2. Click and long click event now also works on ItemView's child views. You can define **onClickIds** and **onLongClickIds** attribute in **@DelegateInfo** injection, or alternative define **ids** attribute in **@OnClick** and **@OnLongClick**.

