package com.gp.base.screen.base

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Bundle
import java.lang.reflect.ParameterizedType


abstract class BaseActivity : AppCompatActivity()