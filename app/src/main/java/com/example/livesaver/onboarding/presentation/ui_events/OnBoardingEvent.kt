package com.example.livesaver.onboarding.presentation.ui_events

sealed class OnBoardingEvent{
    object GetStartedClicked: OnBoardingEvent()
    object SaveAppEntry: OnBoardingEvent()

}

