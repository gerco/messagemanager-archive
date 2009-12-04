/**

 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.queuemanager.ui.util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 * JTextArea with search option. The search option is enabled by default.
 * <p>
 * <code>F3</code> or <code>CTRL-F</code> pops up the search dialog. When
 * something was found or selected, <code>F3</code> looks for the next
 * occurance of the selected text.
 * <p>
 * When the <code>JSearchableTextArea</code> is not editable, the user can
 * type to directly search for the typed text.
 * 
 * @author Gerco Dries (gdr@progaia-rs.nl)
 *
 */
@SuppressWarnings("serial")
public class JSearchableTextArea extends JTextArea {
	private boolean searchEnabled;

	/**
	 * Create a new JSearchableTextArea with the search function enabled.
	 */
	public JSearchableTextArea() {
		setSearchEnabled(true);
	}
	
	/**
	 * Return whether the search function is enabled.
	 * 
	 * @return
	 */
	public boolean isSearchEnabled() {
		return searchEnabled;
	}

	/**
	 * Enable or disable the search function.
	 * 
	 * @param searchEnabled
	 */
	public void setSearchEnabled(boolean searchEnabled) {
		this.searchEnabled = searchEnabled;

		if(isSearchEnabled()) {
			enableSearch();
		}
	}
	
	/**
	 * Enable the search function (registers the keylistener).
	 */
	private void enableSearch() {
		addKeyListener(new SearchKeyAdapter(this));
	}
	
	/**
	 * Key adapter that implements the actual search function.
	 * 
	 * @author Gerco Dries (gdr@progaia-rs.nl)
	 *
	 */
	private static class SearchKeyAdapter extends KeyAdapter {
		private final JTextArea textArea;
		
		/**
		 * Create a new SearchKeyAdapter for the indicated TextArea.
		 * @param textArea
		 */
		public SearchKeyAdapter(final JTextArea textArea) {
			this.textArea = textArea;
		}
		
		/**
		 * When <code>F3</code> or <code>CTRL-F</code> is pressed, ask for 
		 * search input and search.
		 */
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_F3 && e.getModifiers() == 0) {
				String searchString = textArea.getSelectedText();
				if(searchString == null || searchString.length() == 0) {
					searchString = getSearchString(searchString);
				}
				search(searchString, textArea.getSelectionEnd()+1);
			}
			
			if(e.getKeyCode() == KeyEvent.VK_F && e.isControlDown()) {
				String searchString = textArea.getSelectedText();
				searchString = getSearchString(searchString);
				search(searchString, 0);
			}
		}
		
		/**
		 * If the textArea is not editable, perform "search-as-you-type".
		 */
		@Override
		public void keyTyped(KeyEvent e) {
			// If the textArea is editable, ignore keystrokes
			if(textArea.isEditable()) {
				return;
			}
			
			if(e.getKeyChar() == KeyEvent.VK_BACK_SPACE
			|| e.getKeyChar() == KeyEvent.VK_DELETE
			|| e.getKeyChar() == KeyEvent.VK_ENTER
			|| e.getKeyChar() == KeyEvent.VK_ESCAPE) {
				textArea.select(0,0);
			}
			
			String searchString = textArea.getSelectedText();
								
			if(searchString == null)
				searchString = "";
			
			if(!e.isActionKey()) {
				searchString += e.getKeyChar();
			}
			
			search(searchString, textArea.getSelectionStart());
		}
		
		/**
		 * Ask for user input.
		 * 
		 * @param def
		 * @return
		 */
		private String getSearchString(String def) {
			if(def == null)
				def = "";

			return JOptionPane.showInputDialog("Find...", def);
		}
		
		/**
		 * Search for the searchString, starting from startPosition. If the
		 * text is found, select it.
		 * 
		 * @param searchString
		 * @param startPosition
		 */
		private void search(String searchString, int startPosition) {
			if(searchString != null && searchString.length() > 0) {
				searchString = searchString.toLowerCase();
				
				// Find the first occurance at or after the current 
				// start position and select it.
				int position = textArea.getText().toLowerCase().indexOf(
						searchString, 
						Math.max(startPosition, 0));
				if(position >= 0) {
					textArea.select(position, position + searchString.length());
				} else if (startPosition > 0) {
					// If not found and we were searching from a non-zero
					// start position, retry from the top.
					search(searchString, 0);
				}
			}
		}
	}
}
