package com.example.livesaver.onboarding

sealed class OnBoardingEvent{
    object GetStartedClicked:OnBoardingEvent()
    object SaveAppEntry:OnBoardingEvent()

}

