## Changes in mnalis-v37-languageswitch-smoothness branch

This file details changes made in https://github.com/mnalis/StreetComplete/tree/mnalis-v37-languageswitch-smoothness
as modified from upstream https://github.com/streetcomplete/StreetComplete

From `mnalis-v37-languageswitch-smoothness` branch:
* update email/github links to this repository
  so upstream is not bothered with bugs introduced here
* show ALL notes
  when `Show all notes` in preferences is enabled, really **show ALL notes**,
  including your own (only hide notes that user explicitely asked to hide,
  and which can be unhidden via `Restore hidden quests` preference)

From `upstream-mestrona-languageswitch-fixed-v34.2`:
* language chooser, W.I.P.
  see https://github.com/streetcomplete/StreetComplete/pull/3199
* add missing include, so it compiles OK
  see 53697f7bb02605cd6c36a051ee08b7edfb49ed0c

From `remotes/smichel17/undo-button`
* remove extra undo menu, to test
  https://github.com/streetcomplete/StreetComplete/pull/3349

From `helium314/smoothness`:
* Surface smoothness quest, W.I.P.
  https://github.com/westnordost/StreetComplete/issues/1630
  https://github.com/streetcomplete/StreetComplete/pull/3257
* manually try to fix OtherAnswer() refactoring
* try to fix duplicate "quest_presets_delete_message" ?
* try to fix style renames
* QuestTypeAchievement compile error

## TODO
* update APK build code, and put date/version/last commit in filename.
  Also, is it possible to download apk directly from actions, or must it be packed in debug.zip ?
* make `lit` quest available 24/7 again
  https://github.com/streetcomplete/StreetComplete/issues/3248#issuecomment-922493756
* Cherry-pick stuff from `helium314` branch (from https://github.com/Helium314/StreetComplete/tree/mods) (NOTE: there are probably newer version of this patches)
  See https://github.com/streetcomplete/StreetComplete/discussions/3003#discussioncomment-963592
  Newer: https://github.com/Helium314/StreetComplete/blob/my_modifications/README.md ?
  Not included yet (FIXME: needs checking newest branch and documenting; also sometimes older commits are sometimes better as it asks in more situations for website etc):
    * rename app (commit 70a2b3be92533f05beadb83e0a66b07ec6bebfde) - IIRC there is newer rename with `.h3` ?
      (so other debugs don't conflict with my version)
      (but it breaks taking notes in debug version, so beware/test!)
      newer commit b00ed7484fd77f968bb69d6e182333a454e729d1 and reverse 5462928489a6c5afebf3495f5b8ef1b64e645976, is there even newer?
    * add quest profiles (commit 3d97a952d355e2ef4b4ee3c6db0670cda60f5876)
    * add phone number and website quests (commit a01c492bdc1f7385a7b83acd59fed5100fc55cdf)
    * add poi "quests" and adjust quest dot offset (commit 2ea50a4433d9db5b5101a739efd4c9e4282959fc)
    * add gpx notes (commit a01bc6a2ba54fca1c791237571d6b08a4f1ceb2e) -- BUGGY on -dev version no pictures, no easy sharing of notes
    * add button to reverse quest order (commit 1982e79ef618444bc41cb609f9d5270805a24ab2)
    * add 'hide' button  (commit 87fce03ec2d2efaa578920eb79da179c24c86587)
    * add level quests for shopping malls  (commit b2ae6032809b25f206daaf1deb79fe98693e8198)
    * update contact quests  (commit 2dddd0b2d49ffa6c5dc2d67bd629cfcbce8537c5) -- FIXME: it also removes key_cutter, hostel etc, why?
    * replace helium314 reverse quest order button with quick preset change button?
    * adjust dark theme (commit 3251e335c7839520a2eb95b120d6ad88d333f19c)
    * move "none" answer for cycleway to more convenient position (commit 4812c692041d3c3b5ccd4dbbe249132c107317e1)
    * auto-download only if auto-upload is allowed (commit 52cf846a7544c5c1fbf39f1ef0cae8eb93b945ab)
    * add optional zoom with volume buttons (commit 56466f34760db956fd385eef700553f2a3013003)
    * add per-quest settings for some quests (for element selection) (commit a60a389024f7db64091b25c950e511180c3a5ff9)
    * add maxspeed tag selection to per-quest-settings (commit e256a94617d6465db3a6cef92b89ef83e6665d35)
    * x 148a60f1b remove ShowFixme() and other Show() which are in other Helium314 patches we didn't import
* allow house address quest even if no building type ?
  See [issue #2464](https://github.com/streetcomplete/StreetComplete/issues/2464)
* add language selector (or force language to HR in this fork if it is much simpler)
  See https://github.com/streetcomplete/StreetComplete/issues/2964#issuecomment-862609036 and related issues.
* check https://github.com/matkoniecz/Zazolc ?
* https://github.com/Atrate/CompleteStreet
    * show more lit quests
    * show more surface quests
* check https://github.com/streetcomplete/StreetComplete/compare/master...pietervdvn:master ?
* make GitHub build signed version, so upgrade does not mean uninstalling and losing all preferences
  https://medium.com/upday-devs/how-to-setup-github-actions-for-android-projects-a94e8e3b0539
  https://danielllewellyn.medium.com/flutter-github-actions-for-a-signed-apk-fcdf9878f660
  https://github.com/actions/upload-release-asset
  https://github.com/actions/create-release
  https://github.com/ShaunLWM/action-release-debugapk
  https://github.com/ad-m/github-push-action
  https://github.com/r0adkll/sign-android-release
  https://github.com/marketplace/actions/sign-android-release
  https://github.com/marketplace/actions/android-sign
  https://docs.github.com/en/actions/learn-github-actions/security-hardening-for-github-actions
  https://coletiv.com/blog/android-github-actions-setup/
  https://riggaroo.dev/using-github-actions-to-automate-our-release-process/
  https://medium.com/google-developer-experts/github-actions-for-android-developers-6b54c8a32f55
* get icon for phone quest from atrate branch?
  to update `helium314` quest.
  Maybe even check `atrate` code for such quest and merge/replace?
* implement quest for air compressor
  https://github.com/streetcomplete/StreetComplete/issues/3053
* fix images in Notes
  can't save them in helim314 branch, probably due to path changes?
* house type quest, allow caching Residental/Commercial too
* Surface smoothness quest
  https://github.com/westnordost/StreetComplete/issues/1630
  https://github.com/streetcomplete/StreetComplete/pull/3257
* re-enable or manually hardcode quest ordering if it will be removed
  https://github.com/streetcomplete/StreetComplete/issues/3034#issuecomment-879866839
* enable pedastrian crossing checks on more ways?
  https://github.com/streetcomplete/StreetComplete/issues/398#issuecomment-869151327
* import upstream/debug-issue-3312 fix za notes location
  and test it some more https://github.com/streetcomplete/StreetComplete/issues/3312#issuecomment-932605603
  was only: 4836592bce63845ddee06da7990b318f055dfc5e to add location debug info to notes
* remove single-level undo, pa da icon vodi direktno na multi-level undo
  maybe @smichel17 will do -- see https://github.com/streetcomplete/StreetComplete/discussions/3330#discussioncomment-1399451

* FIXME old branches to new branches
    * izvuci CHANGES-mnalis.md iz mnalis-v34.2 i diffaj sa mojim novi iz mnalis-v35) kada je sve gotovo
    * procesljaj cijeli diff upstream-westnordost-v34.2..mnalis-v34.2 da li sam sto propustio
    * make a copy of mnalis-v35 branch, and squash all small things I want in one commit for easier handling
      (how exactly? "git cherry-pick -n" ?)
    * check other unread mails / open tabs
    * in final mnalis-with-everything branch (set as default branch): mnalis-v35 + many helium314 + mestrona-languageswitch + smoothness
