

``` 

	allprojects {
			repositories {
				...
				maven { url 'https://jitpack.io' }
			}
		}
	
	
	dependencies {
	            compile 'com.github.maogedadada:qrphoto:0.0.6'
	  }
```
 
- 开始使用

```

     <com.mao.sortletterlib.SortLetterView
        android:id="@+id/sort_view"
        android:layout_centerVertical="true"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        app:letterColor="@color/colorAccent"
        app:selectLetterColor="@color/yello"
        app:selectBackgroundColor="@color/black"
        app:selectbigtTextColor="@color/btn_blue"
        app:letterSize="15sp"
        app:leftBigText="26sp"
        app:paddingRight="20dp"
        app:iconHeight="49dp"
        app:iconWidth="58dp" />
```
