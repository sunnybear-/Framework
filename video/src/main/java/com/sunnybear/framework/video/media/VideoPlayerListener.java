package com.sunnybear.framework.video.media;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 提供回调的接口
 * Created by chenkai.gu on 2018/3/9.
 */
public abstract class VideoPlayerListener implements
        IMediaPlayer.OnBufferingUpdateListener,
        IMediaPlayer.OnCompletionListener,
        IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnInfoListener,
        IMediaPlayer.OnVideoSizeChangedListener,
        IMediaPlayer.OnErrorListener,
        IMediaPlayer.OnSeekCompleteListener {

}
