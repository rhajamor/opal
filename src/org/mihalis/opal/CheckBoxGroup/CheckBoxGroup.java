/*******************************************************************************
 * Copyright (c) 2011 Laurent CARON
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent CARON (laurent.caron at gmail dot com) - Initial implementation and API
 *******************************************************************************/
package org.mihalis.opal.CheckBoxGroup;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;
import org.mihalis.opal.utils.SWTGraphicUtil;

/**
 * Instances of this class provide an etched border with a title and a checkbox.
 * If the checkbox is checked, the content of the composite is enabled. If the
 * checkbox is unchecked, the content of the composite is disabled, thus not
 * editable.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BORDER</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 */
public class CheckBoxGroup extends Composite {
	private Image oldImage;
	private final Button button;
	private final Composite content;

	/**
	 * Constructs a new instance of this class given its parent and a style
	 * value describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must
	 * be built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code>
	 * style constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 * 
	 * @param parent a widget which will be the parent of the new instance
	 *            (cannot be null)
	 * @param style the style of widget to construct
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                </ul>
	 * 
	 * @see Composite#Composite(Composite, int)
	 * @see SWT#BORDER
	 * @see Widget#getStyle
	 */
	public CheckBoxGroup(final Composite parent, final int style) {
		super(parent, style);

		super.setLayout(new GridLayout());
		this.button = new Button(this, SWT.CHECK);
		final GridData gdButton = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		gdButton.horizontalIndent = 15;
		this.button.setLayoutData(gdButton);
		this.button.setSelection(true);
		this.button.setBackground(getBackground());
		this.button.pack();

		this.button.addSelectionListener(new SelectionAdapter() {

			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (CheckBoxGroup.this.button.getSelection()) {
					activate();
				} else {
					deactivate();
				}
			}
		});

		this.content = new Composite(this, SWT.NONE);
		this.content.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

		this.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				CheckBoxGroup.this.drawWidget();
			}
		});

	}

	/**
	 * Draws the widget
	 */
	private void drawWidget() {
		final Display display = this.getDisplay();
		final Rectangle rect = this.getClientArea();
		final Image newImage = new Image(display, Math.max(1, rect.width), Math.max(1, rect.height));

		final GC gc = new GC(newImage);
		gc.setBackground(getBackground());

		gc.fillRectangle(0, 0, rect.width, rect.height);

		final int margin = (int) (this.button.getSize().y * 1.5);
		final int startY = margin / 2;

		gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		gc.drawRoundRectangle(1, startY, rect.width - 2, rect.height - startY - 2, 2, 2);

		gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		gc.drawRoundRectangle(2, startY + 1, rect.width - 4, rect.height - startY - 4, 2, 2);

		gc.dispose();

		this.setBackgroundImage(newImage);
		if (this.oldImage != null) {
			this.oldImage.dispose();
		}
		this.oldImage = newImage;

	}

	/**
	 * @see org.eclipse.swt.widgets.Composite#getLayout()
	 */
	@Override
	public Layout getLayout() {
		return this.content.getLayout();
	}

	/**
	 * @see org.eclipse.swt.widgets.Composite#setLayout(org.eclipse.swt.widgets.Layout)
	 */
	@Override
	public void setLayout(final Layout layout) {
		this.content.setLayout(layout);
	}

	// ------------------------------------ Getters and Setters
	/**
	 * @return the text of the button
	 */
	public String getText() {
		return this.button.getText();
	}

	/**
	 * @param text the text of the button to set
	 */
	public void setText(final String text) {
		this.button.setText(text);
	}

	/**
	 * @return the font of the button
	 */
	@Override
	public Font getFont() {
		return this.button.getFont();
	}

	/**
	 * @param font the font to set
	 */
	@Override
	public void setFont(final Font font) {
		this.button.setFont(font);
	}

	/**
	 * @return the content of the group
	 */
	public Composite getContent() {
		return this.content;
	}

	/**
	 * @see org.eclipse.swt.widgets.Composite#setFocus()
	 */
	@Override
	public boolean setFocus() {
		return this.content.setFocus();
	}

	/**
	 * Activate the content
	 */
	public void activate() {
		SWTGraphicUtil.getInstance().enable(this.content, true);
	}

	/**
	 * Deactivate the content
	 */
	public void deactivate() {
		SWTGraphicUtil.getInstance().enable(this.content, false);
	}

	/**
	 * @return <code>true</code> if the content is activated, <code>false</code>
	 *         otherwise
	 */
	public boolean isActivated() {
		return this.button.getSelection();
	}

}