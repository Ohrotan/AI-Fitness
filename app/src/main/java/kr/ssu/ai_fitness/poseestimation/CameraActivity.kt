/*
 * Copyright 2018 Zihua Zeng (edvard_hua@live.com), Lang Feng (tearjeaker@hotmail.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.ssu.ai_fitness.poseestimation

import android.app.Activity
import android.os.Bundle
import android.util.Log
import kr.ssu.ai_fitness.R

import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader

/**
 * Main `Activity` class for the Camera app.
 */
class CameraActivity : Activity() {

  private val mLoaderCallback = object : BaseLoaderCallback(this) {
    override fun onManagerConnected(status: Int) {
      when (status) {
        LoaderCallbackInterface.SUCCESS -> isOpenCVInit = true
        LoaderCallbackInterface.INCOMPATIBLE_MANAGER_VERSION -> {
        }
        LoaderCallbackInterface.INIT_FAILED -> {
        }
        LoaderCallbackInterface.INSTALL_CANCELED -> {
        }
        LoaderCallbackInterface.MARKET_ERROR -> {
        }
        else -> {
          super.onManagerConnected(status)
        }
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_camera)

    //test
    Log.v("volley test", "start manager");
    val manager = TrainerVideoAnalysisManager(this, 0)
    manager.getDataFromFile("https://storage.googleapis.com/ai-fitness/tr_video_analysis/new_info.txt");

    if (null == savedInstanceState) {
      //Activity내의 프래그먼트와 상호작용을 하기 위한 인터페이스
      fragmentManager
              //Start a series of edit operations on the Fragments associated with this FragmentManager.
              //프래그먼트와 연결하기 위한 시작(준비) 리턴 타입: FragmentTransaction
          .beginTransaction()
              //이미 있는 프래그먼트를 대체함, add는 중복되는 프래그먼트가 생길 수 있으므로
          .replace(R.id.container, Camera2BasicFragment.newInstance())
              //즉시 커밋되는게 아니라, 메인스레드에 커밋이 실행되기를 요청하는 것(스케줄링을 함)
          .commit()
    }
  }

  override fun onResume() {
    super.onResume()
    if (!OpenCVLoader.initDebug()) {
      OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback)
    } else {
      mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
    }
  }

  companion object {

    init {
      //        System.loadLibrary("opencv_java");
      System.loadLibrary("opencv_java3")
    }

    @JvmStatic
    var isOpenCVInit = false
  }
}
