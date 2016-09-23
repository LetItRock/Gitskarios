package com.alorma.github.ui.activity;

import imgurapiexample.imgurmodel.ImageResponse;
import imgurapiexample.imgurmodel.Upload;
import imgurapiexample.services.ImgurUpload;
import java.io.File;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ContentEditorPresenterTest {

  public static final String LINK = "http://aaa/aaa.png";

  private Scheduler scheduler = Schedulers.immediate();
  private static final String CLIENT_ID = "u09r92";
  private ContentEditorPresenter contentEditorPresenter;

  @Mock ContentEditorPresenter.Callback callback;

  private ImageResponse validImageResponse;
  private File file;
  private ImgurTestApi imgurUpload;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    imgurUpload = spy(new ImgurTestApi());

    file = File.createTempFile("aaa", "aaa");

    contentEditorPresenter = new ContentEditorPresenter(CLIENT_ID, imgurUpload, scheduler, scheduler);
    contentEditorPresenter.setCallback(callback);

    validImageResponse = new ImageResponse();
    validImageResponse.data = new ImageResponse.UploadedImage();
    validImageResponse.data.link = LINK;
    validImageResponse.success = true;
  }

  @Test
  public void shouldCallShowImageLoading_WhenFileIsOk() throws Exception {
    Observable<ImageResponse> obs = Observable.just(validImageResponse);
    when(imgurUpload.uploadImage(any(Upload.class), anyString())).thenReturn(obs);

    contentEditorPresenter.uploadImageWithImgurAPI(file);
    verify(callback).showImageLoading(anyString());
  }

  @Test
  public void shouldCallHideImageLoading_WhenFileIsOk() throws Exception {
    Observable<ImageResponse> obs = Observable.just(validImageResponse);
    when(imgurUpload.uploadImage(any(Upload.class), anyString())).thenReturn(obs);

    contentEditorPresenter.uploadImageWithImgurAPI(file);
    verify(callback).onImageUploaded(anyString(), anyString());
  }

  @Test
  public void shouldCallAppendText_WhenFileIsOk() throws Exception {
    Observable<ImageResponse> obs = Observable.just(validImageResponse);
    when(imgurUpload.uploadImage(any(Upload.class), anyString())).thenReturn(obs);

    contentEditorPresenter.uploadImageWithImgurAPI(file);
    verify(callback).appendText(buildResult(file.getName()));
  }

  @Test
  public void shouldCallShowImageUploadError_WhenReturnsException() throws Exception {
    Observable<ImageResponse> obs = Observable.error(new Exception());
    when(imgurUpload.uploadImage(any(Upload.class), anyString())).thenReturn(obs);

    contentEditorPresenter.uploadImageWithImgurAPI(null);
    verify(callback).showImageUploadError(null);
  }

  private String buildResult(String name) {
    return "\n" + "![" + name + "](" + LINK + ")" + "\n";
  }

  private class ImgurTestApi extends ImgurUpload {
    @Override
    public Observable<ImageResponse> uploadImage(Upload upload, String clientId) {
      return Observable.just(validImageResponse);
    }
  }
}