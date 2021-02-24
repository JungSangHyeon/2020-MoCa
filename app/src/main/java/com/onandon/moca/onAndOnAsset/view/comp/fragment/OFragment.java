package com.onandon.moca.onAndOnAsset.view.comp.fragment;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.onandon.moca.onAndOnAsset.view.comp.OCustomViewCompLifeCycle;
import com.onandon.moca.onAndOnAsset.model.OViewModel;

public abstract class OFragment<T> extends Fragment implements OCustomViewCompLifeCycle, Animator.AnimatorListener {

    // Working Variable
    private boolean isFirstUpdate = true;

    // Associate
        // Model
        protected T model;

    // Component
    private Observer observer;

    /**
     * System Callback
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.createComponent();
        this.onCreate(this.requireActivity());
        this.associateModel(this.getModel());
    }
    protected abstract void createComponent();
    public abstract void onCreate(Activity activity);
    private void associateModel(Class<? extends ViewModel> clazz){
        if(clazz.isAssignableFrom(AndroidViewModel.class)){
            this.model = (T) new ViewModelProvider(this.getViewModelStoreOwner(),
                    ViewModelProvider.AndroidViewModelFactory.getInstance(this.getActivity().getApplication())).get(clazz);
        }else{
            this.model = (T) new ViewModelProvider(this.getViewModelStoreOwner()).get(clazz);
        }
    }
    public abstract ViewModelStoreOwner getViewModelStoreOwner();
    public abstract Class<? extends ViewModel> getModel();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(this.getLayoutId(), container, false);
    }
    public abstract int getLayoutId();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setVisibility(View.INVISIBLE);
        this.associateAndInitView(view);
        this.setModelObserver();
    }
    public abstract void associateAndInitView(View view);
    public void setModelObserver(){
        this.observer = o->this.startUpdate();
        ((OViewModel)this.model).getLiveData().observe(this.getViewLifecycleOwner(), this.observer);
    }
    public void removeModelObserver() {
        ((OViewModel) this.model).getLiveData().removeObserver(this.observer);
    }

    /**
     * Update
     */
    public void startUpdate(){
        if(this.isFirstUpdate){
            this.isFirstUpdate = false;
            this.updateSuper();
            if(this.isAnimationStartCondition()){
                this.startAfterUpdateAnimation();
            }
        }else if(this.isAnimationStartCondition()){
            this.startBeforeUpdateAnimation(this);
        }else{
            this.updateSuper();
        }
    }
    private void updateSuper(){
        this.update();
        this.getView().setVisibility(View.VISIBLE);
    }
    protected boolean isAnimationStartCondition(){return false;}
    protected void startAfterUpdateAnimation(){}
    protected void startBeforeUpdateAnimation(Animator.AnimatorListener startAfterUpdateAnimationListener){}
    protected abstract void update();

    /**
     * Callback
     */
    @Override
    public void onAnimationEnd(Animator animation) {
        this.updateSuper();
        this.startAfterUpdateAnimation();
    }

    /**
     * No Use
     */
    @Override public void onAnimationStart(Animator animation) { }
    @Override public void onAnimationCancel(Animator animation) { }
    @Override public void onAnimationRepeat(Animator animation) { }
}
