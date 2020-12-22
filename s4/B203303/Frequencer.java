package s4.B203303; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID.
import java.lang.*;
import s4.specification.*;
/*package	s4.specification;
ここは、１回、２回と変更のない外部仕様である。
public	interface	FrequencerInterface	{					//	This	interface	provides	the	design	for
frequency	counter.
void	setTarget(byte		target[]);	//	set	the	data	to	search.
void	setSpace(byte		space[]);		//	set	the	data	to	be	searched	target	from.
int	frequency();	//It	return	-1,	when	TARGET	is	not	set	or	TARGET's	length	is	zero
//Otherwise,	it	return	0,	when	SPACE	is	not	set	or	SPACE's	length	is	zero
//Otherwise,	get	the	frequency	of	TAGET	in	SPACE
int	subByteFrequency(int	start,	int	end);
//	get	the	frequency	of	subByte	of	taget,	i.e	target[start],	taget[start+1],	...	,
target[end-1].
//	For	the	incorrect	value	of	START	or	END,	the	behavior	is	undefined.
}
*/

public class Frequencer implements FrequencerInterface {
  //	Code	to	start	with:	This	code	is	not	working,	but	good	start	point	to	work.
  byte  []  myTarget;
  byte  []  mySpace;
  boolean targetReady = false;
  boolean spaceReady  = false;
  int []    suffixArray;          //	Suffix	Array の実装に使うデータの型を int	[]とせよ。
  //	The	variable,	"suffixArray"	is	the	sorted	array	of	all	suffixes	of	mySpace.
  //	Each	suffix	is	expressed	by	a	integer,	which	is	the	starting	position	in	mySpace.
  //	The	following	is	the	code	to	print	the	contents	of	suffixArray.
  //	This	code	could	be	used	on	debugging.
  private void  printSuffixArray()  {
    if(spaceReady)  {
      for(int i=0; i<  mySpace.length; i++)  {
        int s = suffixArray[i];
        for(int j=s; j<mySpace.length; j++) {
          System.out.write(mySpace[j]);
        }
        System.out.write('\n');
      }
    }
  }
  private int suffixCompare(int i,  int j)  {
    //	suffixCompare はソートのための比較メソッドである。
    //	次のように定義せよ。
    //	comparing	two	suffixes	by	dictionary	order.
    //	suffix_i	is	a	string	starting	with	the	position	i	in	"byte	[]	mySpace".
    //	Each	i	and	j	denote	suffix_i,	and	suffix_j.
    //	Example	of	dictionary	order
    //	"i"						<		"o"								:	compare	by	code
    //	"Hi"					<		"Ho"							;	if	head	is	same,	compare	the	next	element
    //	"Ho"					<		"Ho	"						;	if	the	prefix	is	identical,	longer	string	is	big
    //
    //The	 return	 value	 of	 "int	 suffixCompare"	 is	 as	 follows.
    //	if	suffix_i	>	suffix_j,	it	returns	1
    //	if	suffix_i	<	suffix_j,	it	returns	-1
    //	if	suffix_i	=	suffix_j,	it	returns	0;

    if(mySpace[i] > mySpace[j])
      return 1;
    else if(mySpace[i] < mySpace[j])
      return -1;
    else{
      if(i+1 <= mySpace.length-1 && j+1 <= mySpace.length-1)
        return suffixCompare(i+1, j+1);
      else if(j+1 <= mySpace.length-1)
        return -1;
      else return 1;
    }
  }

  public void  setSpace(byte [] space)  {
    //	suffixArray の前処理は、setSpace で定義せよ。
    mySpace = space;
    if(mySpace.length>0)
    spaceReady  = true;
    //	First,	create	unsorted	suffix	array.
    suffixArray = new int[space.length];
    //	put	all	suffixes	in	suffixArray.
    for(int i = 0; i<  space.length; i++)  {
      suffixArray[i]  = i;      //	Please	note	that	each	suffix	is	expressed	by	one integer.
    }

    // for(int i = 0; i < space.length - 1; i++) {
    //   for(int j = i + 1; j < space.length; j++) {
    //     if(suffixCompare(suffixArray[i], suffixArray[j]) == 1) {
    //       int temp = suffixArray[i];
    //       suffixArray[i] = suffixArray[j];
    //       suffixArray[j] = temp;
    //     }
    //   }
    // }
    mergeSort(suffixArray);
  }

  private void merge(int[] a1,int[] a2,int[] a){
    int i = 0,j = 0;
    while(i < a1.length || j < a2.length){
      if(j >= a2.length || (i < a1.length && suffixCompare(a1[i], a2[j]) == -1) ) {
    	   a[i+j]=a1[i];
    	   i++;
      }
      else{
    	   a[i+j]=a2[j];
    	   j++;
      }
    }
  }

  private void mergeSort(int[] a){
    if(a.length > 1){
      int m = a.length / 2;
      int n = a.length - m;
      int[] a1 = new int[m];
      int[] a2 = new int[n];
      for(int i = 0; i < m; i++) a1[i] = a[i];
      for(int i = 0; i < n; i++) a2[i] = a[m + i];
      mergeSort(a1);
      mergeSort(a2);
      merge(a1,a2,a);
    }
  }

  //	Suffix	Array を用いて、文字列の頻度を求めるコード
  //	ここから、指定する範囲のコードは変更してはならない。
  public void  setTarget(byte  []  target) {
    myTarget  = target; if(myTarget.length>0) targetReady = true;
  }

  public int frequency() {
    if(targetReady  ==  false) return -1;
    if(spaceReady ==  false) return 0;
    return subByteFrequency(0, myTarget.length);
  }

  public int subByteFrequency(int start,  int end)  {
    /*	This	method	be	work	as	follows,	but	much	more	efficient
    int	spaceLength	=	mySpace.length;
    int	count	=	0;
    for(int	offset	=	0;	offset<	spaceLength	- (end	- start);	offset++)	{
    boolean	abort	=	false;
    for(int	i	=	0;	i<	(end	- start);	i++)	{
    if(myTarget[start+i]	!=	mySpace[offset+i])	{	abort	=	true;	break;	}
  }
  if(abort	==	false)	{	count++; }
  }
  */
    int first = subByteStartIndex(start,  end);
    int last1 = subByteEndIndex(start,  end);
    return last1 - first;
  }
//	変更してはいけないコードはここまで。
private int targetCompare(int i,  int j,  int k)  {
  //	suffixArray を探索するときに使う比較関数。
  //	次のように定義せよ
  //	suffix_i	is	a	string	in	mySpace	starting	at	i-th	position.
  //	target_i_k	is	a	string	in	myTarget	start	at	j-th	postion	ending	k-th	position.
  //	comparing	suffix_i	and	target_j_k.
  //	if	the	beginning	of	suffix_i	matches	target_i_k,	it	return	0.
  //	The	behavior	is	different	from	suffixCompare	on	this	case.
  //	if	suffix_i	>	target_i_k	it	return	1;
  //	if	suffix_i	<	target_i_k	it	return	-1
  //	It	should	be	used	to	search	the	appropriate	index	of	some	suffix.
  //	Example	of	search
  //	suffix										target
  //	"o"							>					"i"
  //	"o"							<					"z"
  //	"o"							=					"o"
  //	"o"							<					"oo"
  //	"Ho"						>					"Hi"
  //	"Ho"						<					"Hz"
  //	"Ho"						=					"Ho"
  //	"Ho"						<					"Ho	"			:	"Ho	"	is	not	in	the	head	of	suffix	"Ho"
  //	"Ho"						=					"H"					:	"H"	is	in	the	head	of	suffix	"Ho"
  //	ここに比較のコードを書け
  int start_j = j;
  for(int idx = 0; idx < k - start_j; idx++, j++){
    if(suffixArray[i] + idx >= mySpace.length)
      return -1;
  	if(mySpace[suffixArray[i] + idx] >  myTarget[j])
		  return 1;
    else if(mySpace[suffixArray[i]  + idx] <  myTarget[j])
      return -1;
  }
  return 0;
}

private int subByteStartIndex(int start,  int end)  {
  //suffix	array のなかで、目的の文字列の出現が始まる位置を求めるメソッド
  //	以下のように定義せよ。
  /*	Example	of	suffix	created	from	"Hi	Ho	Hi	Ho"
  0:	Hi	Ho
  1:	Ho
  2:	Ho	Hi	Ho
  3:Hi	Ho
  4:Hi	Ho	Hi	Ho
  5:Ho
  6:Ho	Hi	Ho
  7:i	Ho
  8:i	Ho	Hi	Ho
  9:o
  A:o	Hi	Ho
  */
  //	It	returns	the	index	of	the	first	suffix
  //	which	is	equal	or	greater	than	target_start_end.
  //	Assuming	the	suffix	array	is	created	from	"Hi	Ho	Hi	Ho",
  //	if	target_start_end	is	"Ho",	it	will	return	5.
  //	Assuming	the	suffix	array	is	created	from	"Hi	Ho	Hi	Ho",
  //	if	target_start_end	is	"Ho	",	it	will	return	6.
  //
  //	ここにコードを記述せよ。
  //

  // for(int i = 0; i < mySpace.length; i++){
  // if(targetCompare(i, start, end) == 0)
  // return i;
  // }
  //   return -1;

  int idx = binarySearch(0, mySpace.length, start, end);
  if(idx == -1)
    return -1;

  while(--idx >= 0){
    if(targetCompare(idx, start, end) != 0)
      return ++idx;
  }
  return 0;
}

private int binarySearch(int start_idx, int end_idx, int target_start, int target_end){
  int middle_idx = (end_idx - start_idx) / 2 + start_idx;
  int result  = targetCompare(middle_idx, target_start, target_end);
  if(result == 0)
    return middle_idx;
  else if(end_idx - start_idx <= 1)
    return -1;
  else if(result == 1)
  return binarySearch(start_idx, middle_idx, target_start, target_end);
    return binarySearch(middle_idx, end_idx, target_start, target_end);
}

private int subByteEndIndex(int start,  int end)  {
  //suffix	array のなかで、目的の文字列の出現しなくなる場所を求めるメソッド
  //	以下のように定義せよ。
  /*	Example	of	suffix	created	from	"Hi	Ho	Hi	Ho"
  0:	Hi	Ho
  1:	Ho
  2:	Ho	Hi	Ho
  3:Hi	Ho
  4:Hi	Ho	Hi	Ho
  5:Ho
  6:Ho	Hi	Ho
  7:i	Ho
  8:i	Ho	Hi	Ho
  9:o
  A:o	Hi	Ho
  */
  //	It	returns	the	index	of	the	first	suffix
  //	which	is	greater	than	target_start_end;	(and	not	equal	to	target_start_end)
  //	Assuming	the	suffix	array	is	created	from	"Hi	Ho	Hi	Ho",
  //	if	target_start_end	is	"Ho",	it	will	return	7	for	"Hi	Ho	Hi	Ho".
  //	 Assuming	 the	 suffix	 array	 is	 created	 from	 "Hi	 Ho	 Hi	 Ho",
  //	if	target_start_end	is"i",	it	will	return	9	for	"Hi	Ho	Hi	Ho".

  int idx = binarySearch(0, mySpace.length, start, end);
  if(idx == -1)
    return -1;

  while(++idx < mySpace.length){
    if(targetCompare(idx, start, end) != 0)
      return --idx + 1;
  }
  return mySpace.length;
}

//	Suffix	Array を使ったプログラムのホワイトテストは、
//	private なメソッドとフィールドをアクセスすることが必要なので、
//	クラスに属する static	main に書く方法もある。
//	static	main があっても、呼びださなければよい。
//	以下は、自由に変更して実験すること。
//	注意：標準出力、エラー出力にメッセージを出すことは、
//	static	main からの実行のときだけに許される。
//	外部から Frequencer を使うときにメッセージを出力してはならない。
//	教員のテスト実行のときにメッセージがでると、仕様にない動作をするとみなし、
//	減点の対象である。
public static void  main(String[] args) {
  Frequencer frequencerObject;
  try {
    frequencerObject  = new Frequencer();
    // frequencerObject.setSpace("AAA".getBytes());
    frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());

    frequencerObject.printSuffixArray();          //	you	may	use	this	line	for	DEBUG
    /*	Example	from	"Hi	Ho	Hi	Ho"
    0:	Hi	Ho
    1:	Ho
    2:	Ho	Hi	Ho
    3:Hi	Ho
    4:Hi	Ho	Hi	Ho
    5:Ho
    6:Ho	Hi	Ho
    7:i	Ho
    8:i	Ho	Hi	Ho
    9:o
    A:o	Hi	Ho
    */
    // frequencerObject.setTarget("AA".getBytes());
    frequencerObject.setTarget(" H".getBytes());
    //
    //	****		Please	write	code	to	check	subByteStartIndex,	and	subByteEndIndex
    //
    int result  = frequencerObject.frequency();
    System.out.print("Freq	=	"+ result+"	" );

    if(4   ==  result){
      System.out.println("OK");
    }
    else {System.out.println("WRONG"); }
  }
  catch(Exception e)  {
    // System.out.println("STOP");
    System.out.println(e);
  }
}
}
