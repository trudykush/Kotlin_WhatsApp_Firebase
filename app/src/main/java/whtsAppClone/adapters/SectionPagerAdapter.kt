package whtsAppClone.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import whtsAppClone.fragments.ChatsFragments
import whtsAppClone.fragments.UsersFragment

/**
 * Created by Kush on 21/08/2020.
 */
class SectionPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
       return 2
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return UsersFragment()
            }
            1 -> {
                return ChatsFragments()
            }
        }
        return null!!
    }

    override fun getPageTitle(position: Int): CharSequence? {

        when(position) {
            0 -> {
                return "Users"
            }
            1 -> {
                return "Chats"
            }
        }

        return null!!
    }
}