package com.jun.tools.toolkit;

import android.app.Instrumentation;
import android.graphics.PointF;
import android.os.SystemClock;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.MotionEvent.PointerProperties;

import java.util.ArrayList;

/**
 * Test Instrumentation
 */
public class MotionEventToolkit {

	static long EVENT_MIN_INTERVAL = 200;

	private MotionEvent m_motionEvent = null;
	private ArrayList<PointerProperties> m_ptProperties = new ArrayList<PointerProperties>();
	private ArrayList<PointerCoords> m_ptPointerCoords = new ArrayList<PointerCoords>();

	private ArrayList<Integer> m_idList = new ArrayList<Integer>();

	/*
	 * 获取当前MotionEvent，手势改变前后用此方法获取的MotionEvent不一样
	 */
	public MotionEvent getMotionEvent() {
		return m_motionEvent;
	}

	/*
	 * 模拟单指点击
	 * 
	 * @param location 点击位置
	 */
	public void postClick(PointF location, Instrumentation instrumentation) {
		if (m_motionEvent != null && m_idList.size() > 0) {
			return;
		}

		if (location == null) {
			return;
		}

		TouchDescription downTouchDes = new TouchDescription(location, false);
		postTouch(downTouchDes, instrumentation);

		// 由于组件的绘制是利用MOVE事件，所以暂时用MOVE事件模拟
		MoveDescription[] moveDescription = new MoveDescription[] { new MoveDescription(
				location, location, 0) };
		postMoveOnly(moveDescription, 100, instrumentation);

		TouchDescription upTouchDes = new TouchDescription(location, true);
		postTouch(upTouchDes, instrumentation);
	}

	/*
	 * 模拟单指移动，手指按下后移动并抬起
	 * 
	 * @param startPoint 起始点
	 * 
	 * @param desPoint 目标点
	 * 
	 * @param duration 移动花费的时间，单位ms
	 */
	public void postMoveSimply(PointF startPoint, PointF desPoint,
			long duration, Instrumentation instrumentation) {

		if (m_motionEvent != null && m_idList.size() > 0) {
			return;
		}

		if (startPoint == null || desPoint == null) {
			return;
		}

		TouchDescription downTouchDes = new TouchDescription(startPoint, false);
		postTouch(downTouchDes, instrumentation);

		MoveDescription[] moveDescription = new MoveDescription[] { new MoveDescription(
				startPoint, desPoint, 0) };
		postMoveOnly(moveDescription, duration, instrumentation);

		TouchDescription upTouchDes = new TouchDescription(desPoint, true);
		postTouch(upTouchDes, instrumentation);
	}

	/*
	 * 模拟单指按下，不抬起
	 * 
	 * @param downPoint 按下点
	 * 
	 * @param duration 按下手指后，最少停留的时间
	 */
	public void postTouchSimply(PointF downPoint, long duration,
			Instrumentation instrumentation) {
		if (m_motionEvent != null && m_idList.size() > 0) {
			return;
		}

		if (downPoint == null) {
			return;
		}

		TouchDescription downTouchDes = new TouchDescription(downPoint, false);
		// downTouchDes.setDownDuration(duration);
		postTouch(downTouchDes, instrumentation);

		// 由于组件的长按事件是利用MOVE事件，所以暂时用MOVE事件模拟长按
		MoveDescription[] moveDescription = new MoveDescription[] { new MoveDescription(
				downPoint, downPoint, 0) };
		postMoveOnly(moveDescription, duration, instrumentation);
	}

	/*
	 * 模拟双指移动，手指按下后移动并抬起
	 * 
	 * @param startPoint1 第一个手指的起始点
	 * 
	 * @param endPoint1 第一个手指的目标点
	 * 
	 * @param startPoint2 第二个手指的起始点
	 * 
	 * @param endPoint2 第二个手指的目标点
	 * 
	 * @param duration 移动花费的时间，单位ms
	 */
	public void postMoveTwoPointerSimply(PointF startPoint1, PointF endPoint1,
			PointF startPoint2, PointF endPoint2, long duration,
			Instrumentation instrumentation) {

		if (m_motionEvent != null && m_idList.size() > 0) {
			return;
		}

		if (startPoint1 == null || endPoint1 == null || startPoint2 == null
				|| endPoint2 == null || instrumentation == null) {
			return;
		}

		TouchDescription downTouchDes1 = new TouchDescription(startPoint1,
				false);
		postTouch(downTouchDes1, instrumentation);

		TouchDescription downTouchDes2 = new TouchDescription(startPoint2,
				false);
		postTouch(downTouchDes2, instrumentation);

		MoveDescription[] moveDescriptions = new MoveDescription[] {
				new MoveDescription(startPoint1, endPoint1, 0),
				new MoveDescription(startPoint2, endPoint2, 1) };
		postMoveOnly(moveDescriptions, duration, instrumentation);

		TouchDescription upTouchDes1 = new TouchDescription(endPoint1, true);
		postTouch(upTouchDes1, instrumentation);

		TouchDescription upTouchDes2 = new TouchDescription(endPoint2, true);
		postTouch(upTouchDes2, instrumentation);
	}

	/*
	 * 模拟手指在屏幕上移动，手指可以是多个
	 * 
	 * @param moveDescriptions 手指的移动信息，包括移动的起始点、目标点、手指索引
	 * 
	 * @param duration 移动花费的时间，单位ms
	 */
	public void postMoveOnly(MoveDescription[] moveDescriptions, long duration,
			Instrumentation instrumentation) {

		if (moveDescriptions == null || moveDescriptions.length == 0
				|| m_motionEvent == null || instrumentation == null) {
			return;
		}
		int pointerCount = m_idList.size();
		if (pointerCount == 0) {
			return;
		}

		long downTime = m_motionEvent.getDownTime();
		long eventTime = SystemClock.uptimeMillis();
		MotionEvent event = null;
		int moveEventNumber = (int) (duration / EVENT_MIN_INTERVAL);
		if (moveEventNumber == 0) {
			moveEventNumber = 1;
		}

		float[] stepX = new float[moveDescriptions.length];
		float[] stepY = new float[moveDescriptions.length];

		ArrayList<Integer> indexs = new ArrayList<Integer>();

		for (int i = 0; i < moveDescriptions.length; i++) {
			if (moveDescriptions[i] == null) {
				return;
			}

			int index = moveDescriptions[i].getIndex();
			if (index > (pointerCount - 1) || index < 0) {
				return;
			}
			if (indexs.contains(index)) {
				return;
			} else {
				indexs.add(Integer.valueOf(index));
			}

			stepX[i] = (moveDescriptions[i].getEndPoint().x - moveDescriptions[i]
					.getStartPoint().x) / moveEventNumber;
			stepY[i] = (moveDescriptions[i].getEndPoint().y - moveDescriptions[i]
					.getStartPoint().y) / moveEventNumber;
		}

		PointerProperties[] p = new PointerProperties[m_ptProperties.size()];
		m_ptProperties.toArray(p);
		PointerCoords[] c = new PointerCoords[m_ptPointerCoords.size()];
		m_ptPointerCoords.toArray(c);

		event = MotionEvent.obtain(downTime, eventTime,
				MotionEvent.ACTION_MOVE, pointerCount, p, c, 0, 0, 1, 1, 0, 0,
				0, 0);
		instrumentation.sendPointerSync(event);

		for (int i = 0; i < moveEventNumber; i++) {
			// update the move events
			eventTime += EVENT_MIN_INTERVAL;

			for (int m = 0; m < moveDescriptions.length; m++) {
				int index = moveDescriptions[m].getIndex();
				c[index].x += stepX[m];
				c[index].y += stepY[m];
			}

			SystemClock.sleep(EVENT_MIN_INTERVAL);
			event = MotionEvent.obtain(downTime, eventTime,
					MotionEvent.ACTION_MOVE, pointerCount, p, c, 0, 0, 1, 1, 0,
					0, 0, 0);
			instrumentation.sendPointerSync(event);
		}
		SystemClock.sleep(EVENT_MIN_INTERVAL);
		m_motionEvent = event;
	}

	/*
	 * 按下或抬起一手指
	 * 
	 * @param touchDescription 手指触摸信息，包括按下或抬起、手指坐标、按下的时长、抬起手指的索引
	 */
	public void postTouch(TouchDescription touchDescription,
			Instrumentation instrumentation) {

		MotionEvent event = null;
		long downTime = m_motionEvent != null ? m_motionEvent.getDownTime()
				: SystemClock.uptimeMillis();
		long eventTime = SystemClock.uptimeMillis();
		int pointerCount = m_idList.size();
		float x = touchDescription.getLocation().x;
		float y = touchDescription.getLocation().y;
		boolean type = touchDescription.getIsUpOrDown();
		int upIndex = type ? touchDescription.getUpIndex() : -1;
		long duration = type ? 0 : touchDescription.getDownDuration();
		int id = type ? m_idList.get(upIndex) : getNewPointerID();

		PointerProperties pp = new PointerProperties();
		pp.toolType = MotionEvent.TOOL_TYPE_FINGER;
		pp.id = id;
		PointerCoords pc = new PointerCoords();
		pc.x = x;
		pc.y = y;
		pc.pressure = 1;
		pc.size = 1;

		if (m_motionEvent == null) {
			m_ptProperties.clear();
			m_ptPointerCoords.clear();
		}
		if (!type) {
			m_ptProperties.add(pp);
			m_ptPointerCoords.add(pc);
			m_idList.add(id);
		}

		PointerProperties[] p = new PointerProperties[m_ptProperties.size()];
		m_ptProperties.toArray(p);
		PointerCoords[] c = new PointerCoords[m_ptPointerCoords.size()];
		m_ptPointerCoords.toArray(c);

		if (m_motionEvent == null || (type && pointerCount == 1)) {
			int action = type ? MotionEvent.ACTION_UP : MotionEvent.ACTION_DOWN;
			event = MotionEvent.obtain(downTime, eventTime, action, 1, p, c, 0,
					0, 1, 1, 0, 0, 0, 0);
			m_motionEvent = type ? null : event;
		} else {
			pointerCount = type ? pointerCount : (pointerCount + 1);
			int action = type ? MotionEvent.ACTION_POINTER_UP
					: MotionEvent.ACTION_POINTER_DOWN;
			action = action + (pp.id << MotionEvent.ACTION_POINTER_INDEX_SHIFT);
			event = MotionEvent.obtain(downTime, eventTime, action,
					pointerCount, p, c, 0, 0, 1, 1, 0, 0, 0, 0);
			m_motionEvent = event;
		}

		postMotionEvent(event, instrumentation);
		if (type) {
			m_ptProperties.remove(upIndex);
			m_ptPointerCoords.remove(upIndex);
			m_idList.remove(upIndex);
		}
		if (duration == 0) {
			duration = EVENT_MIN_INTERVAL;
		}
		SystemClock.sleep(duration);
	}

	/*
	 * 用于发送事件
	 * 
	 * @param event 用于发送的事件
	 */
	private void postMotionEvent(MotionEvent event,
			Instrumentation instrumentation) {
		try {
			event.setSource(InputDevice.SOURCE_TOUCHSCREEN);// 留意参数
			instrumentation.sendPointerSync(event);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 获取按下手指时对应的id
	 */
	private int getNewPointerID() {
		int id = -1;
		int length = m_idList.size();
		if (length == 0
				|| (m_idList.get(length - 1).intValue() == (length - 1))) {
			id = length;
		} else {
			for (int i = 0; i < length; i++) {
				if (!m_idList.contains(Integer.valueOf(i))) {
					id = i;
					break;
				}
			}
		}
		return id;
	}

	/*
	 * 手指移动时的信息，包括起始点、目标点、手指索引
	 */
	public static class MoveDescription {
		PointF m_startPoint;
		PointF m_endPoint;
		int m_index;

		public MoveDescription() {
			m_startPoint = new PointF();
			m_endPoint = new PointF();
			m_startPoint.x = 0;
			m_startPoint.y = 0;
			m_endPoint.x = 0;
			m_endPoint.y = 0;
			m_index = 0;
		}

		public MoveDescription(PointF startPoint, PointF endPoint, int index) {
			m_startPoint = new PointF();
			m_endPoint = new PointF();
			m_startPoint.x = startPoint.x;
			m_startPoint.y = startPoint.y;
			m_endPoint.x = endPoint.x;
			m_endPoint.y = endPoint.y;
			m_index = index;
		}

		public PointF getStartPoint() {
			return m_startPoint;
		}

		public void setStartPoint(PointF startPoint) {
			this.m_startPoint = startPoint;
		}

		public PointF getEndPoint() {
			return m_endPoint;
		}

		public void setEndPoint(PointF endPoint) {
			this.m_endPoint = endPoint;
		}

		public int getIndex() {
			return m_index;
		}

		public void setIndex(int index) {
			this.m_index = index;
		}
	}

	/*
	 * 手指触摸信息，包括按下或抬起、手指坐标、按下的时长、抬起手指的索引
	 */
	public static class TouchDescription {
		PointF m_location;
		int m_upIndex;
		boolean m_bUpOrDown = false;
		long m_downDuration = 0;

		public TouchDescription() {
			m_location = new PointF();
			m_location.x = 0;
			m_location.y = 0;
			m_upIndex = 0;
		}

		public TouchDescription(PointF location, boolean bUpOrDown) {
			m_location = new PointF();
			m_location.x = location.x;
			m_location.y = location.y;
			m_bUpOrDown = bUpOrDown;
		}

		public PointF getLocation() {
			return m_location;
		}

		public void setLocation(PointF location) {
			this.m_location = location;
		}

		public int getUpIndex() {
			return m_upIndex;
		}

		public void setUpIndex(int index) {
			this.m_upIndex = index;
		}

		public boolean getIsUpOrDown() {
			return m_bUpOrDown;
		}

		public void setIsUpOrDown(boolean bUpOrDown) {
			this.m_bUpOrDown = bUpOrDown;
		}

		public long getDownDuration() {
			return m_downDuration;
		}

		public void setDownDuration(long downDuration) {
			this.m_downDuration = downDuration;
		}
	}
}
