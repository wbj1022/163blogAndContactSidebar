package com.franco.sidebar;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.franco.blog.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class MyAdapter extends BaseAdapter {

	private LayoutInflater inflater;
    private String currentName = "";
	private static Set<Character> charSet = new HashSet<Character>();
	private List<ContactName> contactNameList;
	char firstCharOfCurrentName;
	char firstCharOfPreName;
	
	private class ViewHolder {
        TextView divider;
        TextView contact_name;
    }
	
	public MyAdapter(Context context, List<ContactName>contactNameList) {	
		
		this.inflater = LayoutInflater.from(context);
		this.contactNameList = contactNameList;
		String[] chars = context.getResources().getStringArray(R.array.vailable_charset);
		for (String ch : chars) {
			charSet.add(ch.charAt(0));
        }
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//return super.getView(position, convertView, parent);
		
		ViewHolder holder;
		
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.contact_list_item, null); 
            holder = new ViewHolder();
            holder.divider = (TextView) convertView.findViewById(R.id.divider);  
            holder.contact_name = (TextView) convertView.findViewById(R.id.contact_name);
            convertView.setTag(holder);  
        } else {  
            holder = (ViewHolder) convertView.getTag();  
        }

        currentName = contactNameList.get(position).displayName;
        if(position > 0) {
        	firstCharOfPreName = contactNameList.get(position - 1).firstChar;
        } else {
        	firstCharOfPreName = ' ';
        }
        firstCharOfCurrentName = contactNameList.get(position).firstChar;
        if (firstCharOfCurrentName != firstCharOfPreName) {
            holder.divider.setVisibility(View.VISIBLE);
            holder.divider.setText(String.valueOf(firstCharOfCurrentName));
        } else {  
            holder.divider.setVisibility(View.GONE);  
        }
        holder.contact_name.setText(currentName);
        return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contactNameList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}
