package com.nulldreams.delegateadapter;

import android.content.Context;

import com.nulldreams.delegateadapter.model.Twitter;
import com.nulldreams.delegateadapter.model.Ytb;

/**
 * Created by gaoyunfei on 2016/12/22.
 */

public class Data {

    private static final int[] THUMBS = {
            R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4
    };

    private static final int[] PROFILES = {
            R.drawable.thumb1,
            R.drawable.thumb2,
            R.drawable.thumb3,
            R.drawable.thumb4
    };

    private static final String[] AUTHORS = {
            "泰坦尼克", "The Beatles", "Michael Jackson", "苍井空"
    };
    private static final String[] TITLES = {
            "Movies sell best", "Help", "EarthSong", "童颜巨乳，德艺双馨"
    };
    private static final int[] COUNT = {
            12543, 56873, 9876, 29456
    };
    private static final String[] TEXT = {
            "泰坦尼克号》是美国20世纪福克斯公司和派拉蒙影业公司共同出资，于1994年拍摄的一部浪漫的爱情灾难电影，由詹姆斯·卡梅隆创作、编辑、制作、导演及监制，莱昂纳多·迪卡普里奥、凯特·温斯莱特主演。影片于1997年11月1日在东京首映。",
            "披头士（ 英文：The Beatles ），英国摇滚乐演唱组，1960年成立于英国利物浦，其音乐风格源自美国五十年代的摇滚，之后开拓了各种曲风如迷幻摇滚、流行摇滚、古典音乐的融合等。",
            "Michael为呼吁人类行动起来，保护自然和我们赖以生存的地球而作的伟大单曲。在这首歌中，Michael用淋漓的情感、高亢的高音嘶吼，流着泪诉说着一切正离我们而去的美好事物：“你有没有看到，地球在流泪，海岸在哭泣？一切和平呢？鲜花遍布的田野呢？属于你和我的所有梦想呢？神圣的土地被四分五裂……你是不是忘了在战争中哭着死去的孩子？”",
            "2010年4月11日， 日本著名 AV 女优 苍井空让整个中文推特圈（推特，即 twitter，国外的一个微型 博客网站）热闹起来，不管是男 推友还是女推友，纷纷讨论苍井空。"
    };

    public static Ytb[] getYtbList (Context context, int count) {
        Ytb[] ytbs = new Ytb[count];
        for (int i = 0; i < ytbs.length; i++) {
            int index = i % PROFILES.length;
            ytbs[i] = new Ytb(
                    context,
                    PROFILES[index],
                    THUMBS[index],
                    TITLES[index],
                    AUTHORS[index],
                    COUNT[index]
            );
        }
        return ytbs;
    }

    public static Twitter[] getTwitterList (int count) {
        Twitter[] twitters = new Twitter[count];
        for (int i = 0; i < twitters.length; i++) {
            int index = i % PROFILES.length;
            twitters[i] = new Twitter(
                    PROFILES[index],
                    AUTHORS[index],
                    TEXT[index]
            );
        }
        return twitters;
    }
}
